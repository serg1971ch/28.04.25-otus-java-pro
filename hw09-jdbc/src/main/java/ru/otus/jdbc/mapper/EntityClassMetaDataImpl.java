package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import ru.otus.annotation.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> entity;

    public EntityClassMetaDataImpl(Class<T> entity) {
        this.entity = entity;
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public Constructor<T> getConstructor() {
        Constructor<T> constructor;
        try {
            constructor = entity.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(entity.getDeclaredFields())
                .filter(method -> method.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(entity.getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(entity.getDeclaredFields())
                .filter(method -> !method.isAnnotationPresent(Id.class))
                .toList();
    }
}
