package ru.otus;

import java.util.Arrays;

class TestLoggingImpl implements TestLogging {
    @Log
    @Override
    public void calculation(Object... params) {
        System.out.println(Arrays.toString(params).replace("[", "").replace("]", ""));
    }
}
