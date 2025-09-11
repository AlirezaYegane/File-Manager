package org.example.auth.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.auth.fileIO.TextFileRW;

import java.io.IOException;
import java.lang.reflect.Type;

public class MySerializer {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public <E> String toJson(E input) {
        return gson.toJson(input);
    }

    public <E> E fromJson(String json, Class<E> clazz) {
        return gson.fromJson(json, clazz);
    }

    public <E> E fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public <E> String saveAsJson(E input, String savePath) throws IOException {
        final String json = this.toJson(input);
        TextFileRW.getInstance().write(json, savePath, false); // overwrite
        return json;
    }

    public <E> E loadFromJson(String path, Class<E> clazz) throws IOException {
        String json = TextFileRW.getInstance().read(path);
        if (json == null || json.isEmpty()) return null;
        return this.fromJson(json, clazz);
    }

    public <E> E loadFromJson(String path, Type type) throws IOException {
        String json = TextFileRW.getInstance().read(path);
        if (json == null || json.isEmpty()) return null;
        return this.fromJson(json, type);
    }
}
