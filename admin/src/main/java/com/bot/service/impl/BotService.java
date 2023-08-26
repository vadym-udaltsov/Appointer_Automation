package com.bot.service.impl;

import com.bot.service.IBotService;
import com.commons.model.SetWebHookResult;
import com.commons.service.IHttpService;
import com.commons.service.ISsmService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotService implements IBotService {

    private final IHttpService httpService;
    private final ISsmService ssmService;

    @Autowired
    public BotService(IHttpService httpService, ISsmService ssmService) {
        this.httpService = httpService;
        this.ssmService = ssmService;
    }

    @Override
    public SetWebHookResult registerNewWebHook(String botToken, String departmentId) {
        String botUrl = ssmService.getParameterValue("bot_url");

        String url = "https://api.telegram.org/bot" + botToken + "/setWebhook?url=" + botUrl + departmentId;
        return httpService.getRequest(url, new TypeReference<>() {
        });
    }
}
