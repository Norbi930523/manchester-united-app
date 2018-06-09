package com.udacity.norbi930523.manutdapp.service;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;

import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixturesProvider;
import com.udacity.norbi930523.manutdapp.util.DateUtils;

import java.util.TimeZone;

import timber.log.Timber;

public class CalendarSyncIntentService extends IntentService {

    public static final String BROADCAST_ACTION_STATE_CHANGE = "CalendarSyncIntentService.STATE_CHANGE";
    public static final String BROADCAST_EXTRA_IS_SYNCING = "CalendarSyncIntentService.IS_SYNCING";

    private static final long CALENDAR_ID = 1; // Phone calendar ID

    private static final String ACTION_SYNC_FIXTURES = "com.udacity.norbi930523.manutdapp.service.action.SYNC_FIXTURES";

    private static final String[] FIXTURE_PROJECTION = new String[]{
            FixtureColumns.OPPONENT,
            FixtureColumns.VENUE,
            FixtureColumns.COMPETITION,
            FixtureColumns.DATE
    };

    private static final long FIXTURE_DURATION_MILLIS = DateUtils.MILLIS_IN_HOUR * 2;

    public CalendarSyncIntentService() {
        super("CalendarSyncIntentService");
    }


    public static void startActionSyncFixtures(Context context) {
        Intent intent = new Intent(context, CalendarSyncIntentService.class);
        intent.setAction(ACTION_SYNC_FIXTURES);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SYNC_FIXTURES.equals(action)) {
                handleActionSyncFixtures();
            }
        }
    }

    /* Based on https://developer.android.com/guide/topics/providers/calendar-provider */
    private void handleActionSyncFixtures() {
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        if(writePermission == PackageManager.PERMISSION_GRANTED){
            broadcastStateChange(true);

            ContentResolver contentResolver = getContentResolver();

            /* Delete previously inserted events */
            contentResolver.delete(
                    CalendarContract.Events.CONTENT_URI,
                    CalendarContract.Events.CALENDAR_ID + " = ? AND " + CalendarContract.Events.CUSTOM_APP_PACKAGE + " = ?",
                    new String[]{ String.valueOf(CALENDAR_ID), getApplication().getPackageName() }
             );

            /* Insert events */
            ContentValues[] fixtureEvents = getFixtureEvents(contentResolver);

            for(ContentValues fixtureEvent : fixtureEvents){
                Uri eventUri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, fixtureEvent);

                Long eventId = Long.parseLong(eventUri.getLastPathSegment());

                /* Set reminder */
                ContentValues reminder = getReminderForEvent(eventId);
                contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminder);
            }

            broadcastStateChange(false);
        } else {
            Timber.d("Permission denied: WRITE_CALENDAR");
        }

    }

    private ContentValues getReminderForEvent(Long eventId) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.MINUTES, 15);
        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        return values;
    }

    private ContentValues[] getFixtureEvents(ContentResolver contentResolver){
        Cursor cursor = contentResolver.query(
                FixturesProvider.Fixtures.FIXTURES,
                FIXTURE_PROJECTION,
                null,
                null,
                null
        );

        ContentValues[] valuesArray = new ContentValues[cursor.getCount()];

        int valuesIndex = 0;

        while (cursor.moveToNext()){
            String opponent = cursor.getString(cursor.getColumnIndex(FixtureColumns.OPPONENT));
            String venue = cursor.getString(cursor.getColumnIndex(FixtureColumns.VENUE));
            String competition = cursor.getString(cursor.getColumnIndex(FixtureColumns.COMPETITION));
            long date = cursor.getLong(cursor.getColumnIndex(FixtureColumns.DATE));

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.TITLE, String.format("%s (%s)", opponent, venue));
            values.put(CalendarContract.Events.DESCRIPTION, competition);
            values.put(CalendarContract.Events.DTSTART, date);
            values.put(CalendarContract.Events.DTEND, date + FIXTURE_DURATION_MILLIS);
            values.put(CalendarContract.Events.CALENDAR_ID, CALENDAR_ID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            values.put(CalendarContract.Events.CUSTOM_APP_PACKAGE, getApplicationContext().getPackageName());

            valuesArray[valuesIndex] = values;

            valuesIndex++;
        }

        return valuesArray;
    }

    private void broadcastStateChange(boolean isSyncing){
        Intent stateChange = new Intent(BROADCAST_ACTION_STATE_CHANGE);
        stateChange.putExtra(BROADCAST_EXTRA_IS_SYNCING, isSyncing);

        sendBroadcast(stateChange);
    }

}
