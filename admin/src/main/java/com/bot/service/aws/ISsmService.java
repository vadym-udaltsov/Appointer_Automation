package com.bot.service.aws;

public interface ISsmService {

    String getParameterValue(String parameterName);
}
