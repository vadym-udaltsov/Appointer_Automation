package com.bot.service.impl;

import com.bot.dao.IContextDao;
import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.service.IContextService;
import com.bot.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ContextService implements IContextService {

    private final IContextDao contextDao;

    @Override
    public Context getContext(Update update) {
        return contextDao.getContext(MessageUtils.getUserIdFromUpdate(update));
    }

    @Override
    public void save(Context context) {
        contextDao.saveContext(context);
    }

    @Override
    public void setPhoneNumber(Context context, String number) {
        log.info("Setting phone number: {} to context id: {}", number, context.getUserId());
        contextDao.setPhoneNumber(context, number);
    }

    @Override
    public void updateLocation(Context context, String location) {
        log.info("Adding new location: {} to context id: {}", location, context.getUserId());
        contextDao.updateLocation(context, location);
    }

    @Override
    public void resetLocationToDashboard(Context context) {
        log.info("Resetting location to dashboard");
        contextDao.resetLocationToDashboard(context);
    }

    @Override
    public void removeLastLocation(Context context) {
        List<String> navigation = context.getNavigation();
        log.info("Removing last location: {} from context id: {}", navigation.get(navigation.size() - 1), context.getUserId());
        contextDao.removeLastLocation(context);
    }

    @Override
    public void updateLocale(long id, Language language) {
        contextDao.updateLocale(id, language);
    }
}
