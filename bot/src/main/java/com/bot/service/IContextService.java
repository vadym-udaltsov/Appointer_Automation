package com.bot.service;

import com.commons.model.Appointment;
import com.bot.model.Context;
import com.bot.model.Language;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface IContextService {

    Context getContext(Update update, String departmentId);

    void create(Context context);

    void updateContext(Context context);

    void updateLocale(long id, String departmentId, Language language);

    void removeLastLocation(Context context);

    Context getAdminContext(String phoneNumber, String departmentId);

    void setPreviousStep(Context context);

    void resetLocationToDashboard(Context context);

    void updateLocation(Context context, String location);

    void setPhoneNumber(Context context, String number);

    List<Context> getContextListByAppointments(List<Appointment> appointments);
}
