package ru.otus;


public class Demo {
    public void action() {
        TestLogging testLogging = ProxyFactory.createProxy(new TestLoggingImpl());
        testLogging.calculation(6);
        testLogging.calculation(6, "test");
        testLogging.calculation(6, 7, "example");
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        new Demo().action();
        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения: " + (endTime-startTime) + " мс");
    }
}

