package com.example.demo.controller;

import java.util.Properties;
import java.util.concurrent.Callable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;

import com.example.SampleService;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
  @GetMapping("/")
  public ModelAndView index(ModelAndView mav) throws Throwable {
    mav.setViewName("main/index");
    // Create the authentication configuration
    AuthenticationConfiguration ejbConfig = AuthenticationConfiguration.empty().useName("admin").usePassword("adminadmin");
    // Create the authentication context
    AuthenticationContext context = AuthenticationContext.empty().with(MatchRule.ALL.matchHost("127.0.0.1"), ejbConfig);
    
    // Create a callable that invokes the EJB
    Callable<String> callable = () -> {
      // Create an InitialContext
      Properties properties = new Properties();
      properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
      properties.put(Context.PROVIDER_URL, "remote+http://127.0.0.1:8080");
      InitialContext initialContext = new InitialContext(properties);

      // Look up the EJB and invoke one of its methods
      // Note that this code is the same as before
      SampleService sampleService = (SampleService) initialContext.lookup(
          "ejb:/EARProject-1.0-SNAPSHOT/EJBProject/SampleServiceBean!com.example.SampleService");
      String result = sampleService.sayHello("ejb");
      return result;
    };

    // Use the authentication context to run the callable
    mav.addObject("result", context.runCallable(callable));

    return mav;
  }
}
