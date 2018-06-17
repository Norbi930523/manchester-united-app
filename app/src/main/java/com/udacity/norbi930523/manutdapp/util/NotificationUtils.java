package com.udacity.norbi930523.manutdapp.util;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.udacity.norbi930523.manutdapp.R;

import java.util.Map;

public class NotificationUtils {

    public enum NotificationType{
        GOAL
    }

    private static final String CHANNEL_ID = "default";

    private NotificationUtils(){}

    private static NotificationCompat.Builder getBaseNotification(Context context){
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification);

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_notification)
                .setSound(notificationSound)
                .setAutoCancel(true);

        return notificationBuilder;
    }

    public static void sendNotification(Context context, Map<String, String> data){
        NotificationType notificationType = NotificationType.valueOf(data.get("type"));

        NotificationCompat.Builder notificationBuilder = getBaseNotification(context)
                .setContentTitle(getNotificationTitle(notificationType, context, data))
                .setContentText(getNotificationText(notificationType, context, data));

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationType.ordinal(), notificationBuilder.build());
    }

    private static String getNotificationTitle(NotificationType notificationType, Context context, Map<String, String> data) {
        String title = getTitleByNotificationType(notificationType, context);

        return replaceParameters(title, data);
    }

    private static String getNotificationText(NotificationType notificationType, Context context, Map<String, String> data) {
        String text = getTextByNotificationType(notificationType, context);

        return replaceParameters(text, data);
    }

    private static String replaceParameters(String template, Map<String, String> data){
        for(Map.Entry<String, String> entry : data.entrySet()) {
            template = template.replace(String.format("{%s}", entry.getKey()), entry.getValue());
        }

        return template;
    }

    private static String getTitleByNotificationType(NotificationType notificationType, Context context){
        switch (notificationType){
            case GOAL:
                return context.getString(R.string.goal_title);
            default:
                throw new IllegalArgumentException("Unknown notifiation type: " + notificationType);
        }
    }

    private static String getTextByNotificationType(NotificationType notificationType, Context context) {
        switch (notificationType){
            case GOAL:
                return context.getString(R.string.goal_text);
            default:
                throw new IllegalArgumentException("Unknown notifiation type: " + notificationType);
        }
    }

}
