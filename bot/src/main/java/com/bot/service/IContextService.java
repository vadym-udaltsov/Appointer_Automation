package com.bot.service;

import com.bot.model.Context;
import com.bot.model.Language;
import com.commons.model.Appointment;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface IContextService {

    Context getContext(Update update, String departmentId);

    Context getContext(long userId, String departmentId);

    void create(Context context);

    void delete(Context context);

    void updateContext(Context context);

    void updateLocale(long id, String departmentId, Language language);

    Context getAdminContext(String phoneNumber, String departmentId);

    void setPreviousStep(Context context);

    void resetLocationToDashboard(Context context);

    void updateLocation(Context context, String location);

    List<Context> getContextListByAppointments(List<Appointment> appointments);

    List<Context> getUserContextsByDepartment(Department department);
}
