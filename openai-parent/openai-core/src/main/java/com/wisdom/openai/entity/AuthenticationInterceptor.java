package com.wisdom.openai.entity;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AuthenticationInterceptor implements Interceptor {
    private final String token;

    public AuthenticationInterceptor(String token) {
        this.token = token;
    }

    @NotNull
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request().newBuilder().header("Authorization", "Bearer " + this.token).build();
        return chain.proceed(request);
    }
}
