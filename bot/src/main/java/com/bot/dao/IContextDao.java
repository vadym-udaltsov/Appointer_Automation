package com.bot.dao;

import com.bot.model.Context;
import com.bot.model.Language;
import com.commons.model.Appointment;
import com.commons.model.Department;

import java.util.List;

public interface IContextDao {

    void saveContext(Context context);

    void updateContext(Context context);

    Context getContext(long userId, String departmentId);

    void updateLocale(long id, String departmentId, Language language);

    Context getAdminContext(String phoneNumber, String departmentId);

    void resetLocationToDashboard(Context context);

    void updateLocation(Context context, String location);

    List<Context> getContextListByAppointments(List<Appointment> appointments);

    List<Context> getUserContextsByDepartment(Department department);
}
