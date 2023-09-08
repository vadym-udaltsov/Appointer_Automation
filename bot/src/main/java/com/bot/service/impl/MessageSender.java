package com.bot.service.impl;

import com.bot.service.IMessageSender;
import com.commons.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MessageSender extends TelegramLongPollingBot implements IMessageSender {

    private String botToken;

    @Override
    public void sendMessage(Department department, String message, String chatId) {
        botToken = department.getToken();
        SendMessage sendMessage = new SendMessage(chatId, message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Could not send message to receiver chat: {}", chatId);
            e.printStackTrace();
        }
    }

    @Override
    public void sendPhoto(Department department, String photoId, String chatId) {
        botToken = department.getToken();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(photoId));
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Could not send image to receiver chat: {}", chatId);
            e.printStackTrace();
        }
    }

    @Override
    public void sendLocation(Department department, String chatId) {
        botToken = department.getToken();
        SendLocation sendLocation = new SendLocation();
        sendLocation.setChatId(chatId);
        Location location = department.getLocation();
        sendLocation.setLatitude(location.getLatitude());
        sendLocation.setLongitude(location.getLongitude());
        try {
            execute(sendLocation);
        } catch (TelegramApiException e) {
            log.error("Could not send location to receiver chat: {}", chatId);
            throw new RuntimeException("Could not send location to receiver chat: " + chatId);
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Unused method in Message Sender");
    }

    @Override
    public String getBotUsername() {
        return "";
    }
}
