package com.projects.money_map_api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralValidator {

    public static void validateInput(String input, String fieldName) {
        if (input == null || StringUtils.isEmpty(input.trim()) || StringUtils.isBlank(input.trim())) {
            throw new IllegalArgumentException(String.format(ErrorMessage.EMPTY_INPUT_FIELD, fieldName));
        }
    }
}
