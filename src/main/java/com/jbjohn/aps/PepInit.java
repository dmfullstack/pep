package com.jbjohn.aps;

import com.axiomatics.sdk.connections.BuildablePDPConnection;
import com.axiomatics.sdk.connections.BuildablePDPConnectionFactory;
import com.axiomatics.sdk.connections.aps5.ws.Aps5WsPDPConnectionProperties;
import com.axiomatics.sdk.context.PDPRequestBuilder;
import com.axiomatics.sdk.context.SDKResponse;
import com.axiomatics.xacml.reqresp.attr.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PEP initializer
 */
public class PepInit {

    private static Logger LOGGER = LoggerFactory.getLogger(PepInit.class);

    Aps5WsPDPConnectionProperties pdpConProps;

    PepInit(PepProperties properties) {
        pdpConProps = new Aps5WsPDPConnectionProperties();
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_PDP_DRIVER, properties.getDriver());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_WEBSERVICE_URL, properties.getWsdl());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_TYPE, properties.getTrustStoreType());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_FILE, properties.getTrustStoreFile());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_PASSWORD, properties.getTrustStorePass());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_BASIC_AUTH_USERNAME, properties.getPdpUser());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_BASIC_AUTH_PASSWORD, properties.getPdpPass());
    }

    public int run(String name) {
        /**
         * Defaulting the decision to '2' if the application errors out!
         */
        int decision = 2;
        try {

            BuildablePDPConnection pdpConn3 = BuildablePDPConnectionFactory.getPDPConnection(pdpConProps);

            PDPRequestBuilder request = pdpConn3.getBuilder()
                    .addAttribute(Constants.RESOURCE_CAT, Constants.RESOURCE_ID, name)
                    .setReturnPolicyIdList(false);

            SDKResponse response = pdpConn3.evaluate(request);

            decision = response.getDecision();
        } catch (Exception e) {
            LOGGER.error("Exception fetching decision", e);
        }
        LOGGER.info((char)27 + "[31mDecision : " + decision + " => " + "("
                + getDecisionString(decision) + ")" + (char)27 + "[0m");

        return decision;
    }

    private String getDecisionString(int decision) {
        String decisionString = "";
        if (decision >= 0 && decision < 4) {
            String[] decisionStrings = {"Permit", "Deny", "Indeterminate", "NotApplicable"};
            decisionString = decisionStrings[decision];
        }
        return decisionString;
    }
}
