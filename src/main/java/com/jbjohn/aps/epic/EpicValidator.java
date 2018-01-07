package com.jbjohn.aps.epic;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.jbjohn.aps.pep.PepInit;

import java.util.List;

public class EpicValidator {

    private WorkFlowValidator wfv;

    EpicValidator() {
        wfv = new WorkFlowValidator();
    }

    public JsonObject validate(JsonObject input) {
        String name = input.get("LDAP Users").getAsString();
        String flow = input.get("Authorization Flows").getAsString();

        JsonObject response = new JsonObject();

        List<Integer> authIds;

        authIds = wfv.flow5_4(name);
        boolean notApplicable = false;
        int auth = 2;

        try {
            switch (flow) {
                case "flow_5.4":
                    // Do nothing
                    break;
                case "flow_5.5":
                    authIds = wfv.flow5_5(name, authIds);
                    break;
                case "flow_5.6":
                    authIds = wfv.flow5_6(name, authIds);
                    break;
                case "flow_5.7":
                    authIds = wfv.flow5_7(name, authIds);
                    break;
                case "flow_5.8":
                    authIds = wfv.flow5_8(name, authIds);
                    break;
                default:
                    notApplicable = true;
                    break;
            }

            if (notApplicable) {
                auth = 3;
            } else if (checkPermission(authIds)) {
                auth = 0;
            } else {
                auth = 1;
            }
        } catch (Exception e) {
            // TODO exception logging
        }

        response.add("auth", new JsonPrimitive(PepInit.getDecisionString(auth)));
        response.add("name", new JsonPrimitive(name));
        response.add("flow", new JsonPrimitive(flow));

        return response;
    }

    private boolean checkPermission(List<Integer> ids) {
        return !(ids == null || ids.size() == 0);
    }
}
