package com.commons.service.impl;

import com.commons.service.IHttpService;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HttpService implements IHttpService {

    @Autowired
    private HttpClient httpClient;

    @Override
    public <T> T getRequest(String url, TypeReference<T> type) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            return JsonUtils.parseStringToObject(body, type);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Http request failed");
        }
    }
}
