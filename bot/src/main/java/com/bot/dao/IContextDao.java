package com.bot.dao;

import com.bot.model.Context;
import com.bot.model.Language;

import java.util.Map;

public interface IContextDao {

    void saveContext(Context context);

    void updateContext(Context context);

    Context getContext(long userId, String departmentId);

    void updateLocale(long id, String departmentId, Language language);

    void resetLocationToDashboard(Context context);

    void removeLastLocation(Context context);

    void updateLocation(Context context, String location);

    void setPhoneNumber(Context context, String number);

}
