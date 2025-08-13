package ru.otus.model;

import java.util.List;
import java.util.Optional;

public class ObjectForMessage implements MessagePrototype{
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" + data + "}";
    }

    @Override
    public ObjectForMessage copy() {
        ObjectForMessage result = new ObjectForMessage();
        Optional.ofNullable(data).ifPresent(data -> result.setData(data.stream().toList()));
        return result;
    }
}
