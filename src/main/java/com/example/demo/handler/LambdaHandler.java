package com.example.demo.handler;

import static java.lang.String.join;
import static java.util.stream.Collectors.toMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.demo.PojaApplication;
import com.example.demo.handler.exceptionHandler.ExceptionHandler;
import com.example.demo.handler.exceptionHandler.ExceptionHandlerImpl;
import com.example.demo.handler.model.ResponseEvent.LambdaUrlResponseEvent;
import com.example.demo.handler.model.requestEvent.LambdaUrlRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
public class LambdaHandler
    implements RequestHandler<LambdaUrlRequestEvent, LambdaUrlResponseEvent> {

  @Getter
  private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  private static final String SERVER_PORT = "0";

  private final RequestMappingHandlerAdapter handlerAdapter;
  private final RequestMappingHandlerMapping handlerMapping;
  private final ExceptionHandler<LambdaUrlResponseEvent> exceptionHandler;

  public LambdaHandler() {
    ConfigurableApplicationContext context = applicationContext();
    this.handlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);
    this.handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
    this.exceptionHandler = defaultExceptionHandler();
  }

  @Override
  public LambdaUrlResponseEvent handleRequest(LambdaUrlRequestEvent event, Context context) {
    /*try {
        var request = new HttpServletRequestWrapper(event);
        ServletRequestPathUtils.parseAndCache(request);

        var headers = toMultiValueHeaders(event.getHeaders());

        var responseOutputStream = new ByteArrayOutputStream();
        HttpServletResponseWrapper response =
                new HttpServletResponseWrapper(responseOutputStream, headers);

        var executionChain = handlerMapping.getHandler(request);
        if (executionChain == null) {
            throw new RuntimeException("No handler found for request " + request.getRequestURI());
        }

        var handler = executionChain.getHandler();
        handlerAdapter.handle(request, response, handler);

        var responseBody = responseOutputStream.toString(UTF_8);
        return new LambdaUrlResponseEvent(
                response.getStatus(), flattenHeaders(headers), responseBody);
    } catch (Exception e) {
        return exceptionHandler.handle(e);
    }*/
    log.info("Received lambda request: {}", event);
    return new LambdaUrlResponseEvent(
        200, flattenHeaders(toMultiValueHeaders(event.getHeaders())), "working");
  }

  private ExceptionHandler<LambdaUrlResponseEvent> defaultExceptionHandler() {
    return new ExceptionHandlerImpl();
  }

  private ConfigurableApplicationContext applicationContext() {
    var application = new SpringApplication(PojaApplication.class);
    application.setDefaultProperties(Map.of("server.port", SERVER_PORT));
    return application.run();
  }

  private Map<String, String> flattenHeaders(Map<String, List<String>> headers) {
    return headers != null
        ? headers.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, entry -> join(",", entry.getValue())))
        : new HashMap<>();
  }

  private Map<String, List<String>> toMultiValueHeaders(Map<String, String> singleValueHeaders) {
    return singleValueHeaders != null
        ? singleValueHeaders.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, entry -> List.of(entry.getValue())))
        : new HashMap<>();
  }
}
