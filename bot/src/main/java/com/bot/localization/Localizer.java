package com.bot.localization;

import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Localizer implements ILocalizer {

    private final Map<Language, Map<String, String>> dictionaries = new HashMap<>();
    private final Map<Language, Map<String, String>> keysDictionaries = new HashMap<>();

    @Override
    public void localizeRequest(Update update, Context context) {
        if (context == null) {
            return;
        }
        Language language = context.getLanguage();
        if (language == null || Language.US == language) {
            return;
        }
        Map<String, String> keysDictionary = getKeysDictionary(language);
        localizeUpdate(update, keysDictionary);
    }

    @Override
    public void localizeResponseMessage(List<MessageHolder> holders, Context context) {
        Language language;
        if (context == null || context.getLanguage() == null) {
            language = Language.US;
        } else {
            language = context.getLanguage();
        }

        Map<String, String> dictionary = getDictionary(language);
        for (MessageHolder holder : holders) {
            holder.setMessage(localize(holder.getMessage(), dictionary, holder.getPlaceholders()));
//            List<Button> buttons = holder.getButtons();
//            for (Button button : buttons) {
//                button.setValue(localize(button.getValue(), dictionary, holder.getPlaceholders()));
//            }
        }
    }

    @Override
    public void localizeResponseButtons(SendMessage method, Context context) {
        Language language;
        if (context == null || context.getLanguage() == null) {
            language = Language.US;
        } else {
            language = context.getLanguage();
        }
        Map<String, String> dictionary = getDictionary(language);
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

    private Map<String, String> getKeysDictionary(Language language) {
        Map<String, String> fromCache = keysDictionaries.get(language);
        if (fromCache != null) {
            return fromCache;
        }
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(language.getDeLocalizationPath());
        Map<String, String> dict = JsonUtils.parseInputStreamToObject(resourceAsStream, new TypeReference<>() {
        });
        keysDictionaries.put(language, dict);
        return dict;
    }

    private Map<String, String> getDictionary(Language language) {
        Map<String, String> fromCache = dictionaries.get(language);
        if (fromCache != null) {
            return fromCache;
        }
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(language.getLocalizationFilePath());
        Map<String, String> dict = JsonUtils.parseInputStreamToObject(resourceAsStream, new TypeReference<>() {
        });
        dictionaries.put(language, dict);
        return dict;
    }

    private String localize(String text, Map<String, String> dictionary, Map<String, String> placeholders) {
        localizePlaceholders(dictionary, placeholders);
        String localizedText = localizeString(text, dictionary);
        return substitutePlaceholders(localizedText, placeholders);
    }

    private String substitutePlaceholders(String text, Map<String, String> placeholders) {
        if (placeholders == null) {
            return text;
        }
        StrSubstitutor sub = new StrSubstitutor(placeholders, "${", "}");

        return sub.replace(text);
    }

    private void localizePlaceholders(Map<String, String> dictionary, Map<String, String> placeholders) {
        if (placeholders == null) {
            return;
        }
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            entry.setValue(localizeString(entry.getValue(), dictionary));
        }
    }

    private String localizeString(String key, Map<String, String> dictionary) {
        String localizedText = dictionary.get(key);
        return StringUtils.isBlank(localizedText) ? key : localizedText;
    }
}
