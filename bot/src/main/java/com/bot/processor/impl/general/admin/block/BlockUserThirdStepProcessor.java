package com.bot.processor.impl.general.admin.block;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RequiredArgsConstructor
public class BlockUserThirdStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String cancelSubmit = MessageUtils.getTextFromUpdate(update);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        if (Constants.SUBMIT.equals(cancelSubmit)) {
            String selectedTitle = ContextUtils.getStringParam(context, Constants.SELECTED_TITLE);
            String selectedContextString = ContextUtils.getStringParam(context, selectedTitle);
            Context selectedContext = JsonUtils.parseStringToObject(selectedContextString, Context.class);
            long userId = selectedContext.getUserId();
            appointmentService.deleteAppointmentsByClientId(userId);
            selectedContext.setBlocked(true);
            contextService.updateContext(selectedContext);
            ContextUtils.resetLocationToDashboard(context);
            return List.of(MessageUtils.buildDashboardHolder("Client was blocked", List.of(), strategyKey));
        }
        ContextUtils.setPreviousStep(context);
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(List.of(Constants.SUBMIT));
        return List.of(MessageUtils.holder("Select option from proposed", ButtonsType.KEYBOARD, holderRequest));
    }
}
