package com.bot.dao;

import com.bot.model.Context;
import com.bot.model.Language;

public interface IContextDao {

    void saveContext(Context context);

    Context getContext(long userId);

    void updateLocale(long id, Language language);

    void resetLocationToDashboard(Context context);

    void removeLastLocation(Context context);

    void updateLocation(Context context, String location);

    void setPhoneNumber(Context context, String number);
}
