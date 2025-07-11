package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Проверяем наличие аннотации Log
        if (method.isAnnotationPresent(Log.class)) {
            String params = Arrays.toString(args).replace("[", "").replace("]", "");
            System.out.println("executed method: " + method.getName() + ", param: " + params);
        }
        return method.invoke(target, args);
    }
}
