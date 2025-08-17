package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorEven implements Processor {
    private final TimeStrategy timeStrategy;

    public ProcessorEven(TimeStrategy timeStrategy) {
        this.timeStrategy = timeStrategy;
    }

    @Override
    public Message process(Message message) {
        if ((timeStrategy.getTime() & 1) == 0) {
            throw new RuntimeException("Четная секунда");
        }
        return message;
    }
}
