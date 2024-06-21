package com.konias.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import io.restassured.path.json.JsonPath;

public final class JsonHelper {

    private JsonHelper() {
    }

    public static String getJsonFromFile(String filePath) {
        Path filePathObj = Path.of(filePath);
        try {
            return Files.readString(filePathObj);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file: " + filePath, e);
        }
    }

    public static JsonPath stringToJson(String response){
        JsonPath js1 = new JsonPath(response);
        return js1;
    }

}
