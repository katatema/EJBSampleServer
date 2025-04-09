package com.example;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(SampleService.class)
public class SampleServiceBean {
  public String sayHello(String name) {
    return "Hello " + name;
  }
}
