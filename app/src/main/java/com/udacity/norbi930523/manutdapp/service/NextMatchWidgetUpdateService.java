package com.udacity.norbi930523.manutdapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;

import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixturesProvider;
import com.udacity.norbi930523.manutdapp.util.DateUtils;
import com.udacity.norbi930523.manutdapp.widget.NextMatchInfoVO;
import com.udacity.norbi930523.manutdapp.widget.NextMatchWidget;

public class NextMatchWidgetUpdateService extends IntentService {

    private static final String[] PROJECTION = new String[]{
            FixtureColumns.OPPONENT,
            FixtureColumns.VENUE,
            FixtureColumns.DATE
    };

    private static final String ACTION_UPDATE_NEXT_MATCH = "com.udacity.norbi930523.manutdapp.service.action.UPDATE_NEXT_MATCH";

    public NextMatchWidgetUpdateService() {
        super("NextMatchWidgetUpdateService");
    }

    public static void startActionUpdateNextMatch(Context context) {
        Intent intent = new Intent(context, NextMatchWidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_NEXT_MATCH);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_NEXT_MATCH.equals(action)) {
                handleActionUpdateNextMatch();
            }
        }
    }

    private void handleActionUpdateNextMatch() {
        NextMatchInfoVO nextMatch = getNextMatch();

        /* Update widget */
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NextMatchWidget.class));

        NextMatchWidget.updateWidgets(this, appWidgetManager, appWidgetIds, nextMatch);
    }

    private NextMatchInfoVO getNextMatch(){
        long now = System.currentTimeMillis();

        Cursor cursor = getContentResolver().query(
                FixturesProvider.Fixtures.FIXTURES,
                PROJECTION,
                FixtureColumns.DATE + " >= ?",
                new String[]{ String.valueOf(now) },
                FixtureColumns.DATE + " ASC"
        );

        if(cursor != null && cursor.moveToNext()){
            String opponent = cursor.getString(cursor.getColumnIndex(FixtureColumns.OPPONENT));
            String venue = cursor.getString(cursor.getColumnIndex(FixtureColumns.VENUE));
            long date = cursor.getLong(cursor.getColumnIndex(FixtureColumns.DATE));

            NextMatchInfoVO nextMatch = new NextMatchInfoVO();
            nextMatch.setOpponent(String.format("%s (%s)", opponent, venue));
            nextMatch.setDate(DateUtils.formatDate(date));

            return nextMatch;
        }

        return null;
    }

}
