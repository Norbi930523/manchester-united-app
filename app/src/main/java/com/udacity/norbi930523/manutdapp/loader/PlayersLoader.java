package com.udacity.norbi930523.manutdapp.loader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;

import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.database.news.NewsProvider;
import com.udacity.norbi930523.manutdapp.database.players.PlayerColumns;
import com.udacity.norbi930523.manutdapp.database.players.PlayersProvider;

public class PlayersLoader extends CursorLoader {

    private static final String[] PROJECTION_DETAIL = {
            PlayerColumns._ID,
            PlayerColumns.SQUAD_NUMBER,
            PlayerColumns.LAST_NAME,
            PlayerColumns.FIRST_NAME,
            PlayerColumns.IMAGE_URL,
            PlayerColumns.POSITION,
            PlayerColumns.SQUAD_NUMBER,
            PlayerColumns.BIRTHDATE,
            PlayerColumns.BIRTHPLACE,
            PlayerColumns.JOINED,
            PlayerColumns.JOINED_FROM,
            PlayerColumns.INTERNATIONAL,
            PlayerColumns.APPEARANCES,
            PlayerColumns.GOALS
    };

    private static final String[] PROJECTION_LIST = {
            PlayerColumns._ID,
            PlayerColumns.SQUAD_NUMBER,
            PlayerColumns.LAST_NAME,
            PlayerColumns.IMAGE_URL,
            PlayerColumns.POSITION
    };

    public static PlayersLoader allPlayers(Context context){
        return new PlayersLoader(context, PlayersProvider.Players.PLAYERS, PROJECTION_LIST, null, null, null);
    }

    public static PlayersLoader singlePlayer(Context context, Long playerId){
        return new PlayersLoader(context, PlayersProvider.Players.withId(playerId), PROJECTION_DETAIL, null, null, null);
    }

    private PlayersLoader(@NonNull Context context, @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

}
