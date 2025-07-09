package com.example.demo.handler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorModel(@JsonProperty("message") String message) {
}
