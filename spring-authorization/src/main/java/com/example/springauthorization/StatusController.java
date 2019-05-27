package com.example.springauthorization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class StatusController {

    @GetMapping("/status")
    @ResponseBody
    public Optional<String> getStatus() {
        return Optional.of("ok");
    }
}
