package com.bot.service.impl;

import com.bot.localization.ILocalizer;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.service.IContextService;
import com.bot.service.IMessageSender;
import com.bot.service.ISendMessageService;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SendMessageService implements ISendMessageService {

    private final IMessageSender messageSender;
    private final IContextService contextService;
    private final ILocalizer localizer;

    @Override
    public void sendNotificationToAdmins(List<LString> localizedMessages, Department department) {
        List<String> admins = department.getAdmins();
        String departmentId = department.getId();
        admins.parallelStream().forEach(a -> {
            Context adminContext = contextService.getAdminContext(a, departmentId);
            if (adminContext == null) {
                throw new RuntimeException("Context not found for admin number: " + a);
            }
            String message = localizer.localizeMessage(localizedMessages, adminContext);
            messageSender.sendMessage(department, message, String.valueOf(adminContext.getUserId()));
        });
    }
}
