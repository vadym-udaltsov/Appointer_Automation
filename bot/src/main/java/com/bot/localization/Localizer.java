package com.bot.localization;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import com.bot.service.IDictionaryService;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import software.amazon.awssdk.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Localizer implements ILocalizer {
    private static final String LS = System.lineSeparator();

    private static final Map<String, Map<String, String>> DICTIONARIES = new HashMap<>();

    private final IDictionaryService dictionaryService;

    @Override
    public String localizeMessage(List<LString> messages, Context context, Department department) {
        Map<String, String> dictionary = getDictionary(context.getLanguage(), department);
        StringBuilder result = new StringBuilder();
        for (LString message : messages) {
            result.append(localize(message.getTitle(), dictionary, message.getPlaceholders())).append(LS);
        }
        return result.toString();
    }

    @Override
    public void localizeRequest(Update update, Context context, Department department) {
        if (context == null) {
            return;
        }
        Language language = context.getLanguage();
        if (language == null || Language.US == language) {
            return;
        }
        Map<String, String> keysDictionary = getDictionary(language, department);
        localizeUpdate(update, keysDictionary);
    }

    @Override
    public void localizeResponseMessage(List<MessageHolder> holders, Context context, Department department) {
        Language language;
        if (context == null || context.getLanguage() == null) {
            language = Language.US;
        } else {
            language = context.getLanguage();
        }

        Map<String, String> dictionary = getDictionary(language, department);
        for (MessageHolder holder : holders) {
            List<LString> messages = holder.getMessagesToLocalize();
            if (messages != null && messages.size() > 0) {
                StringBuilder result = new StringBuilder();
                for (LString message : messages) {
                    result.append(localize(message.getTitle(), dictionary, message.getPlaceholders())).append(LS);
                }
                holder.setMessage(result.toString());
                continue;
            }
            holder.setMessage(localize(holder.getMessage(), dictionary, holder.getPlaceholders()));
        }
    }

    @Override
    public void localizeResponseButtons(SendMessage method, Context context, Department department) {
        Language language;
        if (context == null || context.getLanguage() == null) {
            language = Language.US;
        } else {
            language = context.getLanguage();
        }
        Map<String, String> dictionary = getDictionary(language, department);
        ReplyKeyboard keyboard = method.getReplyMarkup();
        if (keyboard instanceof ReplyKeyboardMarkup) {
            ReplyKeyboardMarkup replyMarkup = (ReplyKeyboardMarkup) keyboard;
            List<KeyboardRow> rows = replyMarkup.getKeyboard();
            for (KeyboardRow row : rows) {
                for (KeyboardButton button : row) {
                    button.setText(localizeString(button.getText(), dictionary));
                }
            }
        }
        if (keyboard instanceof InlineKeyboardMarkup) {
            InlineKeyboardMarkup inlineKeyboard = (InlineKeyboardMarkup) keyboard;
            List<List<InlineKeyboardButton>> rows = inlineKeyboard.getKeyboard();
            for (List<InlineKeyboardButton> row : rows) {
                for (InlineKeyboardButton button : row) {
                    button.setText(localizeString(button.getText(), dictionary));
                    button.setCallbackData(localizeString(button.getCallbackData(), dictionary));
                }
            }
        }
    }

    private void localizeUpdate(Update update, Map<String, String> dictionary) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            String data = callbackQuery.getData();
            callbackQuery.setData(localizeString(data, dictionary));
            return;
        }
        Message message = update.getMessage();
        if (message != null) {
            message.setText(localizeString(message.getText(), dictionary));
        }
    }

    private Map<String, String> getDictionary(Language language, Department department) {
        String dictKey = department.getType().name() + "::" + language.name();
        Map<String, String> fromCache = DICTIONARIES.get(dictKey);
        if (fromCache != null) {
            return fromCache;
        }
        Map<String, String> dict = dictionaryService.getDictionary(language, department);
        DICTIONARIES.put(dictKey, dict);
        return dict;
    }

    private String localize(String text, Map<String, String> dictionary, Map<String, String> placeholders) {
        Map<String, String> localizedPlaceholders = localizePlaceholders(dictionary, placeholders);
        String localizedText = localizeString(text, dictionary);
        return substitutePlaceholders(localizedText, localizedPlaceholders);
    }

    private String substitutePlaceholders(String text, Map<String, String> placeholders) {
        if (placeholders == null) {
            return text;
        }
        StrSubstitutor sub = new StrSubstitutor(placeholders, "${", "}");

        return sub.replace(text);
    }

    private Map<String, String> localizePlaceholders(Map<String, String> dictionary, Map<String, String> placeholders) {
        if (placeholders == null) {
            return null;
        }
        Map<String, String> localizedPlaceholders = new HashMap<>();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            localizedPlaceholders.put(entry.getKey(), localizeString(entry.getValue(), dictionary));
        }
        return localizedPlaceholders;
    }

    private String localizeString(String key, Map<String, String> dictionary) {
        String localizedText = dictionary.get(key);
        return StringUtils.isBlank(localizedText) ? key : localizedText;
    }
}
