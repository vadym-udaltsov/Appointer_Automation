package com.bot.service;

import com.bot.model.Context;
import com.bot.model.Language;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IContextService {

    Context getContext(Update update);

    void save(Context context);

    void updateLocale(long id, Language language);

    void removeLastLocation(Context context);

    void resetLocationToDashboard(Context context);

    void updateLocation(Context context, String location);

    void setPhoneNumber(Context context, String number);
}
