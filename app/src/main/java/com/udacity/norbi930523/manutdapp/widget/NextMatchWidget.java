package com.udacity.norbi930523.manutdapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.service.NextMatchWidgetUpdateService;

public class NextMatchWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, NextMatchInfoVO nextMatchInfo) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.next_match_widget);

        if(nextMatchInfo != null){
            views.setTextViewText(R.id.nextMatchOpponent, nextMatchInfo.getOpponent());
            views.setTextViewText(R.id.nextMatchDate, nextMatchInfo.getDate());

            views.setViewVisibility(R.id.nextMatchDate, View.VISIBLE);
        } else {
            views.setTextViewText(R.id.nextMatchOpponent, context.getString(R.string.widget_na));
            views.setViewVisibility(R.id.nextMatchDate, View.GONE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        NextMatchWidgetUpdateService.startActionUpdateNextMatch(context);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, NextMatchInfoVO nextMatchInfo){
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, nextMatchInfo);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

