package com.bot.service;

import com.bot.model.LString;
import com.commons.model.Department;

import java.util.List;

public interface ISendMessageService {

    void sendNotificationToAdmins(List<LString> localizedMessages, Department department);
}
