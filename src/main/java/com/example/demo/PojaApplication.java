package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@PojaGenerated
public class PojaApplication {

  public static void main(String[] args) {
    var application = new SpringApplication(PojaApplication.class);
    application.run();
  }
}
