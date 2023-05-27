package com.bot.service;

import com.bot.model.Context;
import com.bot.model.Language;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IContextService {

    Context getContext(Update update, String departmentId);

    void create(Context context);

    void updateContext(Context context);

    void updateLocale(long id, String departmentId, Language language);

    void skipCurrentStep(Context context, String nextStepKey);

    void removeLastLocation(Context context);

    void setPreviousStep(Context context);

    void resetLocationToDashboard(Context context);

    void updateLocation(Context context, String location);

    void setPhoneNumber(Context context, String number);
}
