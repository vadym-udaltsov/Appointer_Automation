package com.bot.service.impl;

import com.bot.dao.IContextDao;
import com.commons.model.Appointment;
import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ContextService implements IContextService {

    private final IContextDao contextDao;

    @Override
    public Context getContext(Update update, String departmentId) {
        return contextDao.getContext(MessageUtils.getUserIdFromUpdate(update), departmentId);
    }

    @Override
    public void updateContext(Context context) {
        if (context != null) {
            contextDao.updateContext(context);
        }
    }

    @Override
    public Context getAdminContext(String phoneNumber, String departmentId) {
        return contextDao.getAdminContext(phoneNumber, departmentId);
    }

    @Override
    public void create(Context context) {
        contextDao.saveContext(context);
    }

    @Override
    public void setPhoneNumber(Context context, String number) {
        log.info("Setting phone number: {} to context id: {}", number, context.getUserId());
        contextDao.setPhoneNumber(context, number);
    }

    @Override
    public List<Context> getContextListByAppointments(List<Appointment> appointments) {
        return contextDao.getContextListByAppointments(appointments);
    }

    @Override
    public void setPreviousStep(Context context) {
        List<String> navigation = context.getNavigation();
        navigation.remove(navigation.size() - 1);
        updateContext(context);
    }

    @Override
    public void updateLocation(Context context, String location) {
        log.info("Adding new location: {} to context id: {}", location, context.getUserId());
        contextDao.updateLocation(context, location);
    }

    @Override
    public void resetLocationToDashboard(Context context) {
        log.info("Resetting location to dashboard");
        context.setParams(Map.of());
        context.setNavigation(List.of(Constants.Processors.ASK_LANG, Constants.Processors.SET_LANG_ASK_CONT,
                Constants.Processors.SET_CONTACT, Constants.Processors.START_DASH));
        contextDao.updateContext(context);
    }

    @Override
    public void removeLastLocation(Context context) {
        List<String> navigation = context.getNavigation();
        log.info("Removing last location: {} from context id: {}", navigation.get(navigation.size() - 1), context.getUserId());
        contextDao.removeLastLocation(context);
    }

    @Override
    public void updateLocale(long id, String departmentId, Language language) {
        contextDao.updateLocale(id, departmentId, language);
    }

}
