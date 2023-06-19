package com.bot.service;

import com.commons.model.Department;

public interface IMessageSender {

    void sendMessage(Department department, String message, String chatId);
}
