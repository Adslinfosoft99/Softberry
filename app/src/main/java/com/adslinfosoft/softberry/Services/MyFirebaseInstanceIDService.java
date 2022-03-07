package com.adslinfosoft.softberry.Services;//package com.karocall.services;
//
//import android.content.Intent;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.karocall.app.Config;
//
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//
//
///**
// * Created by shreshtha on 20/02/17.
// */
//public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
//    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
//
//    @Override
//    public void onNewToken(String refreshedToken) {
//        super.onNewToken(refreshedToken);
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        registrationComplete.putExtra("token", refreshedToken);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
//
//    }
//
//}
