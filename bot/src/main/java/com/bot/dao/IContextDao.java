package com.bot.dao;

import com.bot.model.Appointment;
import com.bot.model.Context;
import com.bot.model.Language;

import java.util.List;

public interface IContextDao {

    void saveContext(Context context);

    void updateContext(Context context);

    Context getContext(long userId, String departmentId);

    void updateLocale(long id, String departmentId, Language language);

    Context getAdminContext(String phoneNumber, String departmentId);

    void resetLocationToDashboard(Context context);

    void removeLastLocation(Context context);

    void updateLocation(Context context, String location);

    void setPhoneNumber(Context context, String number);

    List<Context> getContextListByAppointments(List<Appointment> appointments);

}
