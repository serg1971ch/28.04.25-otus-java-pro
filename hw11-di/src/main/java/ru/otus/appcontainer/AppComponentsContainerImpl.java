package ru.otus.appcontainer;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;

import java.lang.reflect.Method;
import java.util.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        var beanMethods = stream(configClass.getMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(comparing(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();

        Set<String> registeredComponentNames = new HashSet<>();

        try {
            for (var method : beanMethods) {
                String beanName = method.getAnnotation(AppComponent.class).name();
                if (registeredComponentNames.contains(beanName)) {
                    throw new IllegalArgumentException("Duplicate component name: " + beanName);
                }
                registeredComponentNames.add(beanName);

                Object[] args = stream(method.getParameterTypes())
                        .map(this::getAppComponent)
                        .toArray();

                Object configClassInstance =
                        configClass.getDeclaredConstructor().newInstance();
                Object bean = method.invoke(configClassInstance, args);

                appComponentsByName.put(beanName, bean);
                appComponents.add(bean);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> beans = appComponents.stream()
                .filter(b -> componentClass.isAssignableFrom(b.getClass()))
                .toList();

        if (beans.size() != 1)
            throw new RuntimeException(format("Failed to determine bean in %s", componentClass.getName()));

        return (C) beans.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return ofNullable((C) appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException(format("Failed to determine bean in %s ", componentName)));
    }
}
