package ru.otus.processor;

public class CurrentTime implements TimeStrategy{
    @Override
    public Long getTime() {
        return System.nanoTime() / 1_000_000_000;
    }
}
