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

        try {
            for (var method : beanMethods) {
                Object[] args = stream(method.getParameterTypes())
                        .map(this::getAppComponent)
                        .toArray();

                Object configClassInstance =
                        configClass.getDeclaredConstructor().newInstance();
                Object bean = method.invoke(configClassInstance, args);

                String beanName = method.getAnnotation(AppComponent.class).name();

                appComponentsByName.put(beanName, bean);
                appComponents.add(bean);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object createComponent(Object obj, Method method) {
        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = stream(parameterTypes).map(this::getAppComponent).toArray();
            return method.invoke(obj, args);
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
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException(format("Component with type %s not found!", componentClass)));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(
                        () -> new NoSuchElementException(format("Component with name %s not found!", componentName)));
    }
}
