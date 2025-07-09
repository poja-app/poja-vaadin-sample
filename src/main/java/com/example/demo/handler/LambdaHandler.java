package com.example.demo.handler;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.example.demo.PojaApplication;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;

public class LambdaHandler implements RequestStreamHandler {
  private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse>
      handler;

  static {
    try {
      // Disable WebSocket support
      handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(PojaApplication.class);
      handler.onStartup(
          (servletContext) -> {
            // register vaadin servlet
            VaadinServlet vaadinServlet = new LambdaVaadinServlet();

            ServletRegistration.Dynamic registration =
                servletContext.addServlet("vaadinServlet", vaadinServlet);
            registration.addMapping("/*");
            registration.setAsyncSupported(true);
            registration.setLoadOnStartup(1);
            try {
              vaadinServlet.init(
                  new ServletConfig() {
                    @Override
                    public String getServletName() {
                      return "vaadinServlet";
                    }

                    @Override
                    public ServletContext getServletContext() {
                      return servletContext;
                    }

                    @Override
                    public String getInitParameter(String name) {
                      return null;
                    }

                    @Override
                    public Enumeration<String> getInitParameterNames() {
                      return Collections.emptyEnumeration();
                    }
                  });
            } catch (ServletException e) {
              e.printStackTrace();
              throw new RuntimeException(e);
            }
          });
    } catch (ContainerInitializationException e) {
      e.printStackTrace();
      throw new RuntimeException("Initialization of Spring Boot Application failed", e);
    }
  }

  @Slf4j
  static class LambdaVaadinServlet extends VaadinServlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
      super.init(servletConfig);
      VaadinServletService service = null;
      try {
        service = createServletService();
      } catch (ServiceException e) {
        throw new RuntimeException(e);
      }
      log.info("VaadinServlet initialized as {} with service {}", this, service);
    }

    @Override
    protected boolean serveStaticOrWebJarRequest(
        HttpServletRequest request, HttpServletResponse response) throws IOException {
      return false;
    }
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    handler.proxyStream(inputStream, outputStream, context);
  }
}
