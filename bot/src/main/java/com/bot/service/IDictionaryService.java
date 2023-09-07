package com.bot.service;

import com.bot.model.Language;
import com.commons.model.Department;

import java.util.List;
import java.util.Map;

public interface IDictionaryService {

    List<String> getDictionaryFileKeys(Department department);

    Map<String, String> getDictionary(Language language, Department department);
}
