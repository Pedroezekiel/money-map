package com.projects.money_map_api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessMessage {

    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";

    public static final String PASSWORD_RESET_LINK_SENT = "If an account exists with this email, you will receive a password reset link";
    public static final String ACCOUNT_DELETED_SUCCESSFULLY = "Account deleted successfully";
}
