package com.bot.service;

import com.bot.model.Context;
import com.bot.model.LString;
import com.commons.model.Department;

import java.util.List;

public interface ISendMessageService {

    void sendNotificationToAdmins(List<LString> localizedMessages, Department department);
    void sendNotificationToUsers(List<LString> localizedMessages, List<Context> contextList, Department department);
}
