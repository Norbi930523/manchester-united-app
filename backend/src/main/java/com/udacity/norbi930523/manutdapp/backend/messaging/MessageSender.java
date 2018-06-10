package com.udacity.norbi930523.manutdapp.backend.messaging;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import java.io.InputStream;
import java.util.Map;

public class MessageSender {

    private static final MessageSender INSTANCE = new MessageSender();

    private MessageSender(){
        super();
    }

    public static MessageSender getInstance(){
        return INSTANCE;
    }

    public void sendMessage(Map<String, String> notificationData){
        init();

        for(String token : MessagingTokenManager.getInstance().getRegisteredTokens()){
            Message message = Message.builder()
                    .putAllData(notificationData)
                    .setToken(token)
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Successfully sent message: " + response);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }

        }

    }

    private void init(){
        try{
            InputStream refreshToken = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setDatabaseUrl("https://udacity-man-utd-app.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
