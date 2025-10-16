package me.lahirudilhara.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocController {

    @GetMapping("/")
    public String forwardIndexHtml() {
        return "forward:/index.html";
    }
}
