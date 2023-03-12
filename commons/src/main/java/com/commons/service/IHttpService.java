package com.commons.service;

import com.fasterxml.jackson.core.type.TypeReference;

public interface IHttpService {

   <T> T getRequest(String url, TypeReference<T> type);
}
