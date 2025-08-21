package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    /**
     * группирует выходящий список по name, при этом суммирует поля value
     */
    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> result = new TreeMap<>();
        data.forEach(it -> result.compute(it.getName(), (k, v) -> v != null ? v + it.getValue() : it.getValue()));
        return result;
    }
}
