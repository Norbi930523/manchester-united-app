package com.udacity.norbi930523.manutdapp.database;

import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.database.players.PlayerColumns;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = ManUtdDatabase.VERSION, packageName = "com.udacity.norbi930523.manutdapp.database.generated")
public final class ManUtdDatabase {

    static final int VERSION = 1;

    @Table(ArticleColumns.class) public static final String ARTICLE = "ARTICLE";

    @Table(PlayerColumns.class) public static final String PLAYER = "PLAYER";

    @Table(FixtureColumns.class) public static final String FIXTURE = "FIXTURE";

}
