package ru.otus;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static TestLogging createProxy(TestLogging target) {
        return (TestLogging) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LoggingInvocationHandler(target)
        );
    }
}
