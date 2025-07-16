package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;
    private final Map<String, Set<String>> methodSignatures;
    private Logger logger = Logger.getLogger(LoggingInvocationHandler.class.getName());

    public LoggingInvocationHandler(Object target) {
        this.target = target;
        this.methodSignatures = new HashMap<>();

        Method[] methods = target.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Log.class)) {
                String signature = getMethodSignature(method);
                methodSignatures.computeIfAbsent(method.getName(), k -> new HashSet<>()).add(signature);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String currentSignature = getMethodSignature(method);

        if (methodSignatures.containsKey(method.getName())
                && methodSignatures.get(method.getName()).contains(currentSignature)) {

            String params = formatParams(args);
            logger.info("executed method: " + method.getName() + ", param: " + params);
        }

        return method.invoke(target, args);
    }

    private String getMethodSignature(Method method) {
        StringBuilder signature = new StringBuilder();

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> type : parameterTypes) {
            signature.append(type.getName()).append(",");
        }

        return signature.toString();
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