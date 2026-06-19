package com.kotmin.soldevelo.alertrules.engine.simple;

import com.kotmin.soldevelo.alertrules.engine.AlertEngine;

public class SimpleAlertEngine implements AlertEngine {

    @Override
    public String evaluate(int value) {
        StringBuilder result = new StringBuilder();
        if (value % 3 == 0) result.append("LOW");
        if (value % 5 == 0) result.append("ADVISORY");
        return result.isEmpty() ? String.valueOf(value) : result.toString();
    }
}
