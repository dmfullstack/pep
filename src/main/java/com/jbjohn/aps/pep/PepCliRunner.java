package com.jbjohn.aps.pep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * PEP commandline runner
 */
@Component
public class PepCliRunner implements CommandLineRunner {

    @Autowired
    private PepProperties properties;

    @Override
    public void run(String... strings) throws Exception {

        PepInit pep = new PepInit(properties);
        pep.run(properties.getUserName(), properties.getWorkFlow());
    }
}
