package com.bot.service.impl;

import com.bot.localization.ILocalizer;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.service.IContextService;
import com.bot.service.IMessageSender;
import com.bot.service.ISendMessageService;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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
                log.error("Context not found for admin number: " + a);
                return;
            }
            String message = localizer.localizeMessage(localizedMessages, adminContext, department);
            messageSender.sendMessage(department, message, String.valueOf(adminContext.getUserId()));
        });
    }

    @Override
    public void sendNotificationToUsers(List<LString> localizedMessages, List<Context> contextList, Department department) {
        contextList.parallelStream().forEach(a -> {
            String message = localizer.localizeMessage(localizedMessages, a, department);
            messageSender.sendMessage(department, message, String.valueOf(a.getUserId()));
        });
    }

    @Override
    public void sendPhotoToUsers(String photoFileId, List<Context> contextList, Department department) {
        contextList.parallelStream()
                .forEach(a -> messageSender.sendPhoto(department, photoFileId, String.valueOf(a.getUserId())));
    }
}
