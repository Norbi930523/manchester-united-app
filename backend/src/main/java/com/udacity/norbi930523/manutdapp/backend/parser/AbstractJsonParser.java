package com.udacity.norbi930523.manutdapp.backend.parser;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

abstract class AbstractJsonParser<T> {

    private static final Gson GSON = new Gson();

    public abstract T getJson();

    protected T getJson(String jsonFilename, Class<T> clazz){
        String jsonString = getJsonString(jsonFilename);

        return GSON.fromJson(jsonString, clazz);
    }

    private String getJsonString(String jsonFilename) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(jsonFilename);

        try {
            return IOUtils.toString(is, Charset.forName("UTF-8"));
        } catch (IOException e) {
            return "[]";
        }
    }
}
