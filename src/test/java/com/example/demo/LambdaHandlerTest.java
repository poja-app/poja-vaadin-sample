package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.handler.LambdaHandler;
import com.example.demo.handler.model.requestEvent.Http;
import com.example.demo.handler.model.requestEvent.LambdaUrlRequestEvent;
import com.example.demo.handler.model.requestEvent.RequestContext;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LambdaHandlerTest {
    LambdaHandler handler = new LambdaHandler();

    @Test
    void handle_non_vaadin_event_ok() throws IOException {
        LambdaUrlRequestEvent event = new LambdaUrlRequestEvent();
        RequestContext requestContext = new RequestContext();
        Http http = new Http();
        http.setMethod("GET");
        http.setPath("/ping");
        requestContext.setHttp(http);
        event.setRequestContext(requestContext);
        event.setHeaders(Map.of());
        event.setVersion("1.0");
        event.setRawPath("/ping");
        var response = handler.handleRequest(event, null);

        assertEquals("pong", response.getBody());
    }
}
