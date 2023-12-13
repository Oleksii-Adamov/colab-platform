package com.example.colabplatform.validators;

import com.example.colabplatform.exceptions.ValidationCommonsException;

public class ValidationCommons {
    public static Integer getValidatedPositiveIntegerField(String integerString, String fieldName) throws ValidationCommonsException {
        if (integerString == null) {
            throw new ValidationCommonsException("No parameter " + fieldName);
        }
        Integer parsedInt;
        try {
            parsedInt = Integer.parseInt(integerString);
            if (parsedInt <= 0) throw new ValidationCommonsException("Parameter " + fieldName + " <= 0");
        } catch (NumberFormatException e) {
            throw new ValidationCommonsException("Non integer parameter " + fieldName);
        }
        return parsedInt;
    }

    public static void validateNotNullField(Object obj, String fieldName) throws ValidationCommonsException {
        if (obj == null) {
            throw new ValidationCommonsException("No parameter " + fieldName);
        }
    }

    public static Integer getValidatedMonth(String monthStr) {
        if (monthStr == null) {
            throw new ValidationCommonsException("No parameter month");
        }
        Integer parsedMonth;
        try {
            parsedMonth = Integer.parseInt(monthStr);
            if (parsedMonth < 1 || parsedMonth > 12) throw new ValidationCommonsException("Parameter month <= 0 or > 12");
        } catch (NumberFormatException e) {
            throw new ValidationCommonsException("Non integer parameter month");
        }
        return parsedMonth;
    }

    public static Integer getValidatedYear(String yearStr) {
        return getValidatedPositiveIntegerField(yearStr, "year");
    }
}
