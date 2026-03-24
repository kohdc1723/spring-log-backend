package org.example.springlogbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public String indexApi() {
        return "Welcome to Spring Log server!";
    }
}
