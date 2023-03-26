package com.bot.service.impl;

import com.bot.service.IBotService;
import com.commons.service.IHttpService;
import com.commons.service.ISsmService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class BotService implements IBotService {

    private IHttpService httpService;
    private ISsmService ssmService;

    @Autowired
    public BotService(IHttpService httpService, ISsmService ssmService) {
        this.httpService = httpService;
        this.ssmService = ssmService;
    }

    @Override
    public String registerNewBot(String newBotName) {
        String botToken = "test";

        // Encode the bot's name
        String botName = "MyNewtestudalBot";
        String encodedBotName = null;
        try {
            encodedBotName = URLEncoder.encode(newBotName, "UTF-8");
            String url = "http://api.telegram.org/bot" + botToken + "/newbot?name=" + encodedBotName;
            String response = httpService.getRequest(url, new TypeReference<String>() {
            });
            System.out.println();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Send a request to the BotFather to create a new bot
//        URL url = new URL("https://api.telegram.org/bot" + botToken + "/botfather?text=newbot&botname=" + encodedBotName);
//        ssmService.getParameterValue("api")
//        String urlString = "https://api.telegram.org/bot" + token + "/botfather?text=/newbot%20" + botName;
        return null;
    }

//    @PostConstruct
//    public void test() {
//        registerNewBot("MyNewtestudalBot");
//    }

    @Override
    public void deleteBot(String botName) {
//        String urlString = "https://api.telegram.org/bot" + token + "/botfather?text=/deletebot%20" + botUsername;
    }
}
