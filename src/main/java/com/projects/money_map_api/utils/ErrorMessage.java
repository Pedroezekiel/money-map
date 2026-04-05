package com.projects.money_map_api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {

    public static final String INVALID_LOGIN_CREDENTIALS = "Invalid email or password.";
    public static final String USER_IS_INACTIVE = "User is been inactive.";
    public static final String PASSWORD_DO_NOT_MATCH = "Passwords do not match.";
    public static final String EMAIL_IS_ALREADY_USED = "Email is already used.";
    public static final String NEW_PASSWORDS_DO_NOT_MATCH = "New passwords do not match.";
    public static final String OLD_PASSWORD_IS_INCORRECT = "Old password is incorrect.";
    public static final String NEW_PASSWORD_MUST_BE_DIFFERENT = "New password must be different from the old password.";


}
