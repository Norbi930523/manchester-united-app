package com.udacity.norbi930523.manutdapp.database.fixtures;

import android.net.Uri;

import com.udacity.norbi930523.manutdapp.database.ManUtdDatabase;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = FixturesProvider.AUTHORITY, database = ManUtdDatabase.class, packageName = "com.udacity.norbi930523.manutdapp.database.generated")
public class FixturesProvider {

    public static final String AUTHORITY = "com.udacity.norbi930523.manutdapp.fixtures";

    @TableEndpoint(table = ManUtdDatabase.FIXTURE)
    public static class Fixtures {

        @ContentUri(
                path = "fixtures",
                type = "vnd.android.cursor.dir/list",
                defaultSort = FixtureColumns.DATE + " ASC")
        public static final Uri FIXTURES = Uri.parse("content://" + AUTHORITY + "/fixtures");

        @InexactContentUri(
                path = "fixtures/#",
                name = "FIXTURE_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = FixtureColumns._ID,
                pathSegment = 1)
        public static Uri withId(String fixtureId) {
            return Uri.parse("content://" + AUTHORITY + "/fixtures/" + fixtureId);
        }

    }

}
