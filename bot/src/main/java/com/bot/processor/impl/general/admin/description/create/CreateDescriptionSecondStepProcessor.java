package com.bot.processor.impl.general.admin.description.create;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RequiredArgsConstructor
public class CreateDescriptionSecondStepProcessor implements IProcessor {

    private final IDepartmentService departmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String description = MessageUtils.getTextFromUpdate(update);
        if (description == null || "".equals(description)) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Type description using keyboard", List.of(),
                    KeyBoardType.TWO_ROW, true);
        }
        department.setDescription(description);
        departmentService.updateDepartment(department);
        ContextUtils.resetLocationToStep(context, "descriptionDash");
        return MessageUtils.buildCustomKeyboardHolders("Description added", Constants.ADMIN_APPOINTMENT_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
