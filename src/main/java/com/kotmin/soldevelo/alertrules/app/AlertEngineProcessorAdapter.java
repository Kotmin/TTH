package com.kotmin.soldevelo.alertrules.app;

import com.kotmin.soldevelo.alertrules.engine.AlertEngine;
import java.util.List;

public class AlertEngineProcessorAdapter implements MeasurementProcessor {

    private final AlertEngine engine;

    public AlertEngineProcessorAdapter(AlertEngine engine) {
        this.engine = engine;
    }

    @Override
    public List<String> process(List<Integer> values) {
        return values.stream().map(engine::evaluate).toList();
    }
}
