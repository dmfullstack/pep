package com.jbjohn.aps.controller;

import com.jbjohn.aps.pep.PepInit;
import com.jbjohn.aps.pep.PepProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * PEP request service controller
 */
@RestController
@RequestMapping(value = "/", produces = {"application/json"})
public class PepController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PepController.class);

    @Autowired
    private PepProperties properties;

    @RequestMapping("")
    public Map<String, Object> home() {
        Map<String, Object> model = new HashMap<>();
        model.put("message", "use '/validate' endpoint for validations");
        model.put("request", "request takes 2 params 'name' & 'flow'");
        model.put("example", "use '/validate?name=Elliot&flow=flow_5.4' endpoint for validations");

        return model;
    }

    @RequestMapping("/validate")
    public Map<String, Object> validate(@RequestParam(required = false) String name, @RequestParam(required = false) String flow) {
        Map<String, Object> model = new HashMap<>();

        PepInit pep = new PepInit(properties);
        int decision = pep.run(name, flow);

        model.put("response", decision);
        model.put("message", PepInit.getDecisionString(decision));

        return model;
    }
}
