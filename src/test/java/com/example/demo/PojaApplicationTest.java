package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.handler.LambdaHandler;
import com.example.demo.handler.model.requestEvent.Http;
import com.example.demo.handler.model.requestEvent.LambdaUrlRequestEvent;
import com.example.demo.handler.model.requestEvent.RequestContext;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class PojaApplicationTest {
    LambdaHandler handler = new LambdaHandler();

    @Test
    void test() throws IOException {
        LambdaUrlRequestEvent event = new LambdaUrlRequestEvent();
        RequestContext requestContext = new RequestContext();
        Http http = new Http();
        http.setMethod("GET");
        http.setPath("/");
        requestContext.setHttp(http);
        event.setRequestContext(requestContext);
        event.setVersion("1.0");
        event.setRawPath("/");
        var response = handler.handleRequest(event, null);

        assertEquals("HTML CONTENT", response.getBody());
    }
}
