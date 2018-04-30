package com.udacity.norbi930523.manutdapp.database.players;

import android.net.Uri;

import com.udacity.norbi930523.manutdapp.database.ManUtdDatabase;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = PlayersProvider.AUTHORITY, database = ManUtdDatabase.class, packageName = "com.udacity.norbi930523.manutdapp.database.generated")
public class PlayersProvider {

    public static final String AUTHORITY = "com.udacity.norbi930523.manutdapp.players";

    @TableEndpoint(table = ManUtdDatabase.PLAYER)
    public static class Players {

        @ContentUri(
                path = "players",
                type = "vnd.android.cursor.dir/list",
                defaultSort = PlayerColumns.POSITION + " ASC, " + PlayerColumns.SQUAD_NUMBER + " ASC")
        public static final Uri PLAYERS = Uri.parse("content://" + AUTHORITY + "/players");

        @InexactContentUri(
                path = "players/#",
                name = "PLAYER_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = PlayerColumns._ID,
                pathSegment = 1)
        public static Uri withId(Long playerId) {
            return Uri.parse("content://" + AUTHORITY + "/players/" + playerId);
        }
    }

}
