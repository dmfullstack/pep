package com.jbjohn.aps.epic;

import java.util.List;

class WorkFlowValidator {

    // TODO cache the results for a while
    private List<Integer> isEmployee(final String name) {
        List<Integer> us_boeing_employee = null;
        // TODO check LDAP for user location and employer

        return us_boeing_employee;
    }

    List<Integer> flow5_4(String name) {
        List<Integer> result = null;

        List<Integer> employee = isEmployee(name);
        if (employee != null && employee.size() > 0) {
            return employee;
        } else {
            // TODO check LDAP for user location and employer
        }
        return result;
    }

    List<Integer> flow5_5(String name, List<Integer> authIds) {
        List<Integer> result = null;

        List<Integer> employee = isEmployee(name);
        if (employee != null && employee.size() > 0) {
            return employee;
        } else {
            // TODO check LDAP for user location and employer
        }
        return result;
    }

    List<Integer> flow5_6(String name, List<Integer> authIds) {
        List<Integer> result = null;

        List<Integer> employee = isEmployee(name);
        if (employee != null && employee.size() > 0) {
            return employee;
        } else {
            // TODO check LDAP for user location and employer
        }
        return result;
    }

    List<Integer> flow5_7(String name, List<Integer> authIds) {
        List<Integer> result = null;

        List<Integer> employee = isEmployee(name);
        if (employee != null && employee.size() > 0) {
            return employee;
        } else {
            // TODO check LDAP for user location and employer
        }
        return result;
    }

    List<Integer> flow5_8(String name, List<Integer> authIds) {
        List<Integer> result = null;

        List<Integer> employee = isEmployee(name);
        if (employee != null && employee.size() > 0) {
            return employee;
        } else {
            // TODO check LDAP for user location and employer
        }
        return result;
    }
}
