package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

public class PojaApplicationStarterTest {
  @Test
  void run() {
    var application = new SpringApplication(PojaApplication.class);
    application.run();
  }
}
