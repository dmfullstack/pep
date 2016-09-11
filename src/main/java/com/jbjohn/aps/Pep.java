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
 * Hello world!
 */
public class Pep {

    private static Logger LOGGER = LoggerFactory.getLogger(Pep.class);

    Aps5WsPDPConnectionProperties pdpConProps;

    Pep(PepProperties properties) {
        pdpConProps = new Aps5WsPDPConnectionProperties();
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_PDP_DRIVER, properties.getDriver());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_WEBSERVICE_URL, properties.getWsdl());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_TYPE, properties.getTrustStoreType());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_FILE, properties.getTrustStoreFile());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_PASSWORD, properties.getTrustStorePass());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_BASIC_AUTH_USERNAME, properties.getPdpUser());
        pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_BASIC_AUTH_PASSWORD, properties.getPdpPass());
    }

    public void run(String name) {
        try {

            BuildablePDPConnection pdpConn3 = BuildablePDPConnectionFactory.getPDPConnection(pdpConProps);

            PDPRequestBuilder request = pdpConn3.getBuilder()
                    .addAttribute(Constants.RESOURCE_CAT, Constants.RESOURCE_ID, name)
                    .setReturnPolicyIdList(false);

            SDKResponse response = pdpConn3.evaluate(request);

            LOGGER.info((char)27 + "[31mDecision : " + response.getDecision() + " => " + "("
                    + getDecisionString(response.getDecision()) + ")" + (char)27 + "[0m");

        } catch (Exception e) {
            LOGGER.error("Exception fetching decision", e);
        }
    }

    private static String getDecisionString(int decision) {
        String decisionString = "";
        if (decision >= 0 && decision < 4) {
            String[] decisionStrings = {"Permit", "Deny", "Indeterminate", "NotApplicable"};
            decisionString = decisionStrings[decision];
        }
        return decisionString;
    }
}
