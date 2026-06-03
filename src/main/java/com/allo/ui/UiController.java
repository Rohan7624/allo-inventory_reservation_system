package com.allo.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/")
    public String home() {
        return "products";
    } // all products will be displayed on the home page
    // api/products endpoint will be called to fetch the products and display them on the home page

    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }
}