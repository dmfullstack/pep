package com.jbjohn.aps;

import com.axiomatics.sdk.connections.BuildablePDPConnection;
import com.axiomatics.sdk.connections.BuildablePDPConnectionFactory;
import com.axiomatics.sdk.connections.PDPConnectionException;
import com.axiomatics.sdk.connections.aps5.ws.Aps5WsPDPConnectionProperties;
import com.axiomatics.sdk.context.PDPRequestBuilder;
import com.axiomatics.sdk.context.SDKPolicyIdentifier;
import com.axiomatics.sdk.context.SDKResponse;
import com.axiomatics.sdk.context.SDKResponseWithTrace;
import com.axiomatics.xacml.reqresp.Advice;
import com.axiomatics.xacml.reqresp.AttributeAssignment;
import com.axiomatics.xacml.reqresp.Category;
import com.axiomatics.xacml.reqresp.Obligation;
import com.axiomatics.xacml.reqresp.Status;
import com.axiomatics.xacml.reqresp.attr.Constants;
import org.w3c.dom.Element;

import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Hello world!
 */
public class Pep {

    public static void main(String[] args) {
        try {

            Aps5WsPDPConnectionProperties pdpConProps = new Aps5WsPDPConnectionProperties();
            pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_PDP_DRIVER,
                    "com.axiomatics.sdk.connections.aps5.ws.Aps5WsPDPConnection");
            pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_WEBSERVICE_URL,
                    "https://ecfd.vagrant-local.jbj:8643/asm-pdp/pdp?wsdl");
            pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_TYPE,
                    "jks");
            pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_FILE,
                    "/home/jacobbj/code/pep/certificate/ca-truststore.jks");
            pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_TRUSTSTORE_PASSWORD,
                    "jbjohn");
            pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_BASIC_AUTH_USERNAME,
                    "pdp-user");
            pdpConProps.setProperty(Aps5WsPDPConnectionProperties.KEY_APS5_BASIC_AUTH_PASSWORD,
                    "%+}req*\"");

            BuildablePDPConnection pdpConn3 = BuildablePDPConnectionFactory.getPDPConnection(pdpConProps);

            /**
             * The following code creates a simple request object using 5 attributes.
             * Three of them, namely SUBJECT_ID, RESOURCE_ID and ACTION_ID are
             * added to the corresponding pre-defined categories. The fourth
             * attribute "OperatingSystemName" is a user defined attribute added
             * to the ENVIRONMENT category. The fifth attribute, which is also
             * user defined and having the name "MyAttributeName" and the value
             * "MyAttributeValue", is added to a custom category named
             * "MyCategory"
             *
             */
            System.out.println("Generating an example PDPRequestBuilder ...");
            PDPRequestBuilder request = pdpConn3.getBuilder()
//                    .addSubjectAttribute(Constants.SUBJECT_ID, "Ebi")
//                    .addResourceAttribute(Constants.RESOURCE_ID, "res")
//                    .addActionAttribute(Constants.ACTION_ID, "READ")
//                    .addEnvironmentAttribute("OperatingSystemName", "Windows")
                    .addAttribute(Constants.SUBJECT_CAT, Constants.SUBJECT_ID, "Jacob")
                    .setReturnPolicyIdList(true);

            /**
             * The code below evaluates the request and obtains an SDKResonse.
             * In order to obtain a valid response, 1) A PDP should be up and
             * running 2) A policy which can evaluate the request should be
             * deployed on that PDP.The method SDKResonse.getDecision() returns
             * the response obtained. It is an integer whose value can be
             * Result.DECISION_PERMIT(0), Result.DECISION_DENY(1),
             * Result.DECISION_INDETERMINATE(2),
             * Result.DECISION_NOT_APPLICABLE(3), Result.INVALID_DECISION(4)
             *
             */

            // Evaluate the request
            System.out.println("Evaluating the Request using PDPConnection.evaluate(request)...");
            SDKResponse response = pdpConn3.evaluate(request);

            // Access the decision from SDKResponse
            System.out.println("Reading the decision using SDKResponse.getDecision() : " + response.getDecision() + "("
                    + getDecisionString(response.getDecision()) + ")");

            // Access the SDKResponse as a String
            String responseString = response.getSDKResponseString();
            System.out.println("Response string");
            System.out.println(responseString);

            /**
             * In order to identify this request in the PDP's server logs, it is
             * is necessary to associate a transaction-identifier to the XACML
             * request while we evaluate it. This transactionId can be used as a
             * reference in the PDP logs. The following code demonstrates
             * evaluation of a request with a transactionId. This feature is
             * available only on APS 5.2 and higher version. A
             * PDPConnectionException will be thrown if the method is called on
             * an older version of the PDP.
             *
             * In this and subsequent example we use the request.copy() method because
             * the PDPRequestBuilder object is frozen internally in the evaluate method,
             * thus reusing the same PDPRequestBuilder object as previously would throw an
             * exception.

             */

            // Evaluate the request with a transactionId
            try {
                System.out
                        .println("Evaluating the Request using BuildablePDPConnection.evaluate(request,transactionId)...");
                String transactionId = "TransactionIdentifier";
                response = pdpConn3.evaluate(request.copy(), transactionId);
            } catch (PDPConnectionException e) {
                System.out
                        .println("Evaluation of a request with a transaction-id is supported only by PDP Version 5.2 or higher");
                e.printStackTrace();
            }

            /**
             * It is also possible to evaluate the request and obtain Trace
             * information on how the request was processed by the PDP as below.
             * The trace is a DOM Element. It can be converted to a String using
             * the library XMLUtility available along with this product. The
             * code snippet below shows how the Trace can be accessed.
             */

            // Evaluate with Trace
            System.out.println("Evaluating the Request with trace using PDPConnection.evaluateWithTrace(request)...");
            SDKResponseWithTrace responseWithTrace = pdpConn3.evaluateWithTrace(request.copy());

            // Access the original Xacml Response
            System.out.println("Reading the decision using SDKResponseWithTrace.getDecision() : "
                    + responseWithTrace.getDecision());

            // Access the Trace information
            Element trace = responseWithTrace.getTrace();
            String traceString = responseWithTrace.getTraceString();
            // System.out.println("String trace" + traceString);

            /**
             * In addition to trace, several other properties are provided for
             * the SDKResponse. These correspond to the XACML Specification and
             * the code below provides a quick overview of these features.
             */

            // Read the set of Obligations to fulfill for the result
            System.out.println("Reading the Obligations for Result...");
            Set<? extends Obligation> obligations = response.getObligations();
            if (obligations != null) {
                // Iterate through every Obligation
                Iterator<? extends Obligation> obligationIterator = obligations.iterator();
                while (obligationIterator.hasNext()) {
                    Obligation obligation = obligationIterator.next();
                    if (obligation != null) {
                        System.out.println(" Obligation - Id: " + obligation.getId() + " fulfillOn: "
                                + obligation.getFulfillOn());
                        // Read the list of AttributeAssignments for every
                        // Obligation
                        List<?> attributeAssignments = (List<?>) obligation.getAssignments();
                        // Iterate through every AttributeAssignment
                        if (attributeAssignments != null) {
                            for (int i = 0; i < attributeAssignments.size(); i++) {
                                AttributeAssignment attributeAssignment = (AttributeAssignment) attributeAssignments
                                        .get(i);
                                if (attributeAssignment != null) {
                                    System.out.println("  AttributeAssignment - attributeId: "
                                            + attributeAssignment.getAttributeId() + " value: "
                                            + attributeAssignment.getValue().encodeIntoSimpleString() + " category: "
                                            + attributeAssignment.getCategory() + " issuer: "
                                            + attributeAssignment.getIssuer());
                                }
                            }
                        }
                    }
                }
            }

            // Read the set of Advice to consider for the result
            System.out.println("Reading the Advice for Result...");
            Set<? extends Advice> adviceSet = response.getAdvice();
            if (adviceSet != null) {
                // Iterate through every Advice
                Iterator<? extends Advice> adviceIterator = adviceSet.iterator();
                while (adviceIterator.hasNext()) {
                    Advice advice = adviceIterator.next();
                    if (advice != null) {
                        System.out.println(" Advice - Id: " + advice.getId() + " fulfillOn: " + advice.getFulfillOn());
                        // Read the list of AttributeAssignments for every
                        // Advice
                        List<?> attributeAssignments = (List<?>) advice.getAssignments();
                        // Iterate through every AttributeAssignment
                        if (attributeAssignments != null) {
                            for (int i = 0; i < attributeAssignments.size(); i++) {
                                AttributeAssignment attributeAssignment = (AttributeAssignment) attributeAssignments
                                        .get(i);
                                if (attributeAssignment != null) {
                                    System.out.println("  AttributeAssignment - attributeId: "
                                            + attributeAssignment.getAttributeId() + " value: "
                                            + attributeAssignment.getValue().encodeIntoSimpleString() + " category: "
                                            + attributeAssignment.getCategory() + " issuer: "
                                            + attributeAssignment.getIssuer());
                                }
                            }
                        }
                    }
                }
            }

            // Along with the Response, the PDP returns the PolicyIdentifiers of
            // the Policies and PolicySets used while evaluating the Request. To
            // obtain this list, the returnPolicyIdList property of the request
            // should be set to true. The code below creates a new request with
            // this indicator set to true and then reads the PolicyIdentifiers
            // from the SDKResponse

            PDPRequestBuilder requestWithPolicyIdList = pdpConn3.getBuilder()
                    .addSubjectAttribute(Constants.SUBJECT_ID, "sub")
                    .addResourceAttribute(Constants.RESOURCE_ID, "res")
                    .addActionAttribute(Constants.ACTION_ID, "act")
                    .setReturnPolicyIdList(true);

            SDKResponse responseWithPolicyIdList = pdpConn3.evaluate(requestWithPolicyIdList);

            System.out.println("Reading the Policy Identifier List...");
            List<? extends SDKPolicyIdentifier> policyIdentiferList = responseWithPolicyIdList.getPolicyIdList();
            for (SDKPolicyIdentifier policyIdentifer : policyIdentiferList) {
                System.out.println(" PolicyIdentifier - policyId: " + policyIdentifer.getId() + " policyVersion: "
                        + policyIdentifer.getVersion() + " isPolicySet?: " + policyIdentifer.isPolicySet());
            }

            // Fetch the set of Categories and Attributes in the original
            // request, which yielded this Result
            Set<? extends Category> attributes = response.getIncludedAttributes();

            // Read the status of the decision in the result
            Status status = response.getStatus();
            System.out.println("Status received : " + status);

        } catch (Exception e) {
            e.printStackTrace();
            /**
             * A PDPConnectionException can be thrown as the result of a
             * SocketTimeoutException occurring if the the time taken to obtain
             * the connection exceeds timeout.connect or if the time taken to
             * obtain the evaluation result exceeds timeout.read. Both these
             * values are specified in the connection properties file.
             *
             * The following code snippet checks if SocketTimeoutException is
             * the cause. an appropriate action to handle the situation can
             * further be derived at this point.
             */
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof SocketTimeoutException) {
                    // Handle the timeout appropriately
                }
                cause = cause.getCause();
            }
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
