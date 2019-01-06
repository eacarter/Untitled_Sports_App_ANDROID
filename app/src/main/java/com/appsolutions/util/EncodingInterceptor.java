package com.appsolutions.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EncodingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        ResponseBody modifiedBody = ResponseBody.create(mediaType, response.body().bytes());
        return response.newBuilder().body(modifiedBody).build();
    }
}
