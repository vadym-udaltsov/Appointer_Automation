package com.bot.service;

import java.io.UnsupportedEncodingException;

public interface IBotService {

    String registerNewBot(String newBotName) throws UnsupportedEncodingException;

    void deleteBot(String botName);
}
