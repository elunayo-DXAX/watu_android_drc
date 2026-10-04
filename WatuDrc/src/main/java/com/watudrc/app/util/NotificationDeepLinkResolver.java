package com.watudrc.app.util;

/**
 * Single source of truth for mapping a push-notification "category" to the MainActivity
 * ViewPager tab it should open, shared by the status-bar-tap deeplink path
 * (MainActivity.handleTabSelection) and the in-app notifications list (FragmentNotifications).
 */
public class NotificationDeepLinkResolver {

    public static final int TAB_HOME = 0;
    public static final int TAB_TRANSACTIONS = 1;
    public static final int TAB_PAYMENTS = 2;
    public static final int TAB_NOTIFICATIONS = 3;
    public static final int TAB_SUPPORT = 4;

    public static final String CATEGORY_LIVECHAT = "livechat";

    private NotificationDeepLinkResolver() {
    }

    public static boolean isLiveChatCategory(String category) {
        return category != null && CATEGORY_LIVECHAT.equalsIgnoreCase(category);
    }

    public static int resolveTabForCategory(String category) {
        if (category == null) return -1;
        switch (category.toLowerCase()) {
            case "payment":
            case "reminder":
                return TAB_PAYMENTS;
            case "transaction":
            case "transactions":
                return TAB_TRANSACTIONS;
            case "notification":
            case "notifications":
                return TAB_NOTIFICATIONS;
            case "support":
            case "resources":
                return TAB_SUPPORT;
            default:
                return -1;
        }
    }
}
