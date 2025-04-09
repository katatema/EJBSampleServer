package com.example;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;

@Stateless
@Remote(SampleService.class)
public class SampleServiceBean {
  public String sayHello(String name) {
    return "Hello " + name;
  }
}
