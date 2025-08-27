package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HelloWorldController {
    @GetMapping("/hello-world")
    public Map<String, String> helloWorld() {
        return Map.of("message", "helloworld");
    }
}