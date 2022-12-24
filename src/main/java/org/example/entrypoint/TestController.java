package org.example.entrypoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  private int counter = 0;

  @GetMapping("/test")
  public int counterRoute() {
    return ++counter;
  }
}
