package com.kotmin.soldevelo.alertrules;

import com.kotmin.soldevelo.alertrules.app.AlertEngineProcessorAdapter;
import com.kotmin.soldevelo.alertrules.config.AlertEngineFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        String ruleSet = "base";
        List<Integer> values = defaultValues();

        for (String arg : args) {
            if (arg.startsWith("--rules=")) {
                ruleSet = arg.substring("--rules=".length());
            } else if (arg.startsWith("--values=")) {
                String csv = arg.substring("--values=".length());
                values = Arrays.stream(csv.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
            }
        }

        var engine = AlertEngineFactory.createDefault(ruleSet);
        var processor = new AlertEngineProcessorAdapter(engine);
        processor.process(values).forEach(System.out::println);
    }

    private static List<Integer> defaultValues() {
        return IntStream.rangeClosed(1, 20).boxed().collect(Collectors.toList());
    }
}
