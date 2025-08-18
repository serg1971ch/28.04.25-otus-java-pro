package ru.otus.dataprocessor;

import com.google.gson.Gson;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import ru.otus.model.Measurement;

import java.util.Arrays;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    /**
     * читает файл, парсит и возвращает результат
     */
    @Override
    public List<Measurement> load() {
        List<Measurement> result;
        Gson gson = new Gson();
        try (JsonReader reader =
                Json.createReader(ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName))) {
            String jsonString = reader.read().toString();
            result = Arrays.asList(gson.fromJson(jsonString, Measurement[].class));
        }
        return result;
    }
}
