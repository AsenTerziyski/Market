package com.test.market.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String helloMarket() {
        return "HELLO MARKET!";
    }
}
