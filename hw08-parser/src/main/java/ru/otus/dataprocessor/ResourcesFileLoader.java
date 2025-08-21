package ru.otus.dataprocessor;

import com.google.gson.Gson;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    List<Measurement> result;
    Gson gson = new Gson();

    @Override
    public List<Measurement> load() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileProcessException("Resource not found: " + fileName);
            }

            result = Arrays.asList(gson.fromJson(new InputStreamReader(inputStream), Measurement[].class));

        } catch (IOException e) {
            throw new FileProcessException("Error reading or parsing file: {} " + fileName);
        }
        return result;
    }
}
