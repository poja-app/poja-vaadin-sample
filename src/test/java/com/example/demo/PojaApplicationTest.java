package com.example.demo;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.serverless.proxy.model.HttpApiV2HttpContext;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequestContext;
import com.example.demo.handler.LambdaHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class PojaApplicationTest {
  LambdaHandler handler = new LambdaHandler();

  @Test
  void test() throws IOException {
    var body = new HttpApiV2ProxyRequest();
    body.setRawPath("/");
    body.setVersion("1.0");
    HttpApiV2ProxyRequestContext requestContext = new HttpApiV2ProxyRequestContext();
    HttpApiV2HttpContext http = new HttpApiV2HttpContext();
    http.setMethod("GET");
    requestContext.setHttp(http);
    body.setRequestContext(requestContext);
    var inputStream =
        new ByteArrayInputStream(
            new ObjectMapper().findAndRegisterModules().writeValueAsBytes(body));

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    handler.handleRequest(inputStream, outputStream, null);

    assertEquals("HTML CONTENT", outputStream.toString(UTF_8));
  }
}
