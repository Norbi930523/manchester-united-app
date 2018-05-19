package com.udacity.norbi930523.manutdapp.loader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;

import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixturesProvider;

public class FixturesLoader extends CursorLoader {

    private static final String[] PROJECTION_LIST = {
            FixtureColumns._ID,
            FixtureColumns.DATE,
            FixtureColumns.COMPETITION,
            FixtureColumns.OPPONENT,
            FixtureColumns.VENUE,
            FixtureColumns.RESULT
    };

    public static FixturesLoader fixturesByDateRange(Context context, long startDate, long endDate){
        return new FixturesLoader(context,
                FixturesProvider.Fixtures.FIXTURES,
                PROJECTION_LIST,
                FixtureColumns.DATE + " >= ? AND " + FixtureColumns.DATE + " <= ?",
                new String[]{ String.valueOf(startDate), String.valueOf(endDate) },
                null);
    }

    private FixturesLoader(@NonNull Context context, @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

}
