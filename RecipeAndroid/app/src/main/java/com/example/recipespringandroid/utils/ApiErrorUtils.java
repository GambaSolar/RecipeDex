package com.example.recipespringandroid.utils;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ApiErrorUtils {

    public static String getErrorMessage(Throwable t) {

        if (t instanceof HttpException) {
            ResponseBody body = ((HttpException) t).response().errorBody();
            try {
                return body != null ? body.string() : "Error desconocido";
            } catch (IOException e) {
                return "Error leyendo respuesta";
            }
        }

        return t.getMessage() != null ? t.getMessage() : "Error desconocido";
    }
}