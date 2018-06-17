package com.udacity.norbi930523.manutdapp.messaging;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.udacity.norbi930523.manutdapp.util.NotificationUtils;

import java.util.Map;

public class ManUtdMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationUtils.sendNotification(this, remoteMessage.getData());
    }
}
