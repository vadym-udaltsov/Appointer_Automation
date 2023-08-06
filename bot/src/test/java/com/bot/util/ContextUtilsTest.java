package com.bot.util;

import com.bot.model.Context;
import com.commons.model.Department;
import com.commons.model.DepartmentType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContextUtilsTest {

//    @Test
    void getStrategyKey() {
        Context context = new Context();
        context.setPhoneNumber("0667882320");
        Department department = new Department();
        department.setType(DepartmentType.MASSAGE);
        List<String> adminsList = new ArrayList<>();
        adminsList.add("0667882320");
        department.setAdmins(adminsList);

        String key = ContextUtils.getStrategyKey(context, department);
        assertEquals("MASSAGE::ADMIN", key);
    }
}