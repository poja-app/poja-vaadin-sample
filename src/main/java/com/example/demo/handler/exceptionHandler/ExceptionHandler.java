package com.example.demo.handler.exceptionHandler;


public interface ExceptionHandler<R> {
    R handle(Throwable throwable);
}
