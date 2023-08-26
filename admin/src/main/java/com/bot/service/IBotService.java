package com.bot.service;

import com.commons.model.SetWebHookResult;

public interface IBotService {

    SetWebHookResult registerNewWebHook(String botToken, String departmentId);
}
