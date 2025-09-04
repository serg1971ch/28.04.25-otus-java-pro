package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private static final String SELECT_ALL = "SELECT * FROM %s";
    private static final String SELECT_BY_ID = "SELECT %s FROM %s WHERE %s = ?";
    private static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE = "UPDATE %s SET %s WHERE %s = ?";

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return format(SELECT_ALL, getTableName());
    }

    @Override
    public String getSelectByIdSql() {
        return format(SELECT_BY_ID, getAllFields(), getTableName(), getIdField());
    }

    @Override
    public String getInsertSql() {
        return format(INSERT, getTableName(), getFieldsWithoutId(), getValuesWithoutId());
    }

    @Override
    public String getUpdateSql() {
        return format(UPDATE, getTableName(), getFieldsToUpdate(), getIdField());
    }

    private String getTableName() {
        String[] packageNameAsArray = entityClassMetaData.getName().split("\\.");
        return packageNameAsArray[packageNameAsArray.length - 1].toLowerCase();
    }

    private String getIdField() {
        return entityClassMetaData.getIdField().getName();
    }

    private String getAllFields() {
        return entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(", "));
    }

    private String getFieldsWithoutId() {
        return entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(", "));
    }

    private String getValuesWithoutId() {
        return entityClassMetaData.getFieldsWithoutId().stream().map(field -> " ? ").collect(Collectors.joining(", "));
    }

    private String getFieldsToUpdate() {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
    }
}
