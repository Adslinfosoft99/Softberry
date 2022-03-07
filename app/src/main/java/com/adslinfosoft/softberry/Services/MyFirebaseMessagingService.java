package com.adslinfosoft.softberry.Services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.adslinfosoft.softberry.activity.notification.NotificationActivity;
import com.adslinfosoft.softberry.app.Config;
import com.adslinfosoft.softberry.app.Softberry;
import com.adslinfosoft.softberry.db.FetchData;
import com.adslinfosoft.softberry.model.NotificationVO;
import com.adslinfosoft.softberry.Utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import androidx.annotation.NonNull;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String EMPTY = "";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String TIME = "time";
    private static final String DATE = "date";
    private static final String DATETIME = "datetime";
    private static final String DATA = "data";
    private static final String SOS = "issos";
    private static final String ACTION_DESTINATION = "action_destination";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        storeRegIdInPref(token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences.Editor editor = Softberry.getPreference().edit();
        editor.putString("regId", token);
        editor.apply();
    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();

    }

    private void handleData(Map<String, String> data) {
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String time = data.get(TIME);
        String date = data.get(DATE);
        String datetime = data.get(DATETIME);
        String actionDestination = data.get(ACTION_DESTINATION);
        NotificationVO notificationVO = new NotificationVO();
        if (TextUtils.isEmpty(iconUrl)) {
            notificationVO.setTime(time);
            notificationVO.setMessage(message);
            notificationVO.setTitle(title);
            notificationVO.setIsRead(0);
            new FetchData().insertNotification(notificationVO);
        } else {
            notificationVO.setMessage(message);
            notificationVO.setImageURL(iconUrl);
            notificationVO.setTitle(title);
            notificationVO.setTime(time);
            notificationVO.setAction(action);
            notificationVO.setActionDestination(actionDestination);
        }

        Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();


    }

}
