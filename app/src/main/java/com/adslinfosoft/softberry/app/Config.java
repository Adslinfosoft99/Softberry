package com.adslinfosoft.softberry.app;

/**
 * Created by shreshtha on 20/02/17.
 */

public class Config {

    // old sha1 :: 0D:A0:9A:F7:A2:73:E5:F0:BA:32:32:CB:E4:79:9F:6A:F4:49:EF:5B
    // new sha1 :: 0D:A0:9A:F7:A2:73:E5:F0:BA:32:32:CB:E4:79:9F:6A:F4:49:EF:5B


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}
