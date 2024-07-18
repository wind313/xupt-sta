package com.sta.xuptsta.holder;

public class CurrentUserIdHolder {
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        currentUserId.set(userId);
    }

    public static Long getCurrentUserId() {
        return currentUserId.get();
    }
}
