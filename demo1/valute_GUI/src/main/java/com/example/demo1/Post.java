package com.example.demo1;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Post {

    OkHttpClient okHttpClient = new OkHttpClient();

    public String getResponse(String url){
        String result = "";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
