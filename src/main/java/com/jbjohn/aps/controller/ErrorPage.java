package com.jbjohn.aps.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 */
@RestController
public class ErrorPage implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH, produces = {"application/json"})
    public Map<String, Object> error() {
        Map<String, Object> model = new HashMap<>();
        model.put("message", "invalid request, use '/validate' endpoint for validations");
        model.put("request", "request takes 2 params 'name' & 'flow'");
        model.put("example", "use '/validate?name=Elliot&flow=flow_5.4' endpoint for validations");

        return model;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
