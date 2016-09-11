package com.jbjohn.aps;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * PEP properties
 */
@Component
@ConfigurationProperties(prefix = "pep")
@Data
public class PepProperties {
    private String driver;
    private String wsdl;
    private String trustStoreType;
    private String trustStoreFile;
    private String trustStorePass;
    private String pdpUser;
    private String pdpPass;
    private String username;
}
