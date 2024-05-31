package com.aditya.visitorproject.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExtraController {

    @GetMapping("test")
    public String testT(){
        return "yes yes";
    }
}
