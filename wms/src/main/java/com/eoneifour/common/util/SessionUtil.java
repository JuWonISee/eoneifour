package com.eoneifour.common.util;

import com.eoneifour.shopadmin.user.model.User;

public class SessionUtil {
    private static User loginUser;

    public static void setLoginUser(User user) {
        loginUser = user;
    }

    public static User getLoginUser() {
        return loginUser;
    }

    public static void clear() {
        loginUser = null;
    }
}