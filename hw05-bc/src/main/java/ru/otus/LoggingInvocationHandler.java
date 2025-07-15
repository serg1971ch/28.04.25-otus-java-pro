package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;
    private final Set<String> loggedMethods;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
        this.loggedMethods = new HashSet<>();

        Method[] methods = target.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Log.class)) {
                loggedMethods.add(method.getName());
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (loggedMethods.contains(method.getName())) {
            String params = formatParams(args);
            System.out.println("executed method: " + method.getName() + ", param: " + params);
        }

        return method.invoke(target, args);
    }

    private String formatParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }

        StringBuilder params = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                params.append(", ");
            }
            params.append("param").append(i + 1).append(": ").append(args[i]);
        }
        return params.toString();
    }
}
