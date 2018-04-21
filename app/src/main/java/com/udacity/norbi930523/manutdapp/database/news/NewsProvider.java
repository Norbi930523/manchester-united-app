package com.udacity.norbi930523.manutdapp.database.news;

import android.net.Uri;

import com.udacity.norbi930523.manutdapp.database.ManUtdDatabase;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = NewsProvider.AUTHORITY, database = ManUtdDatabase.class, packageName = "com.udacity.norbi930523.manutdapp.database.generated")
public class NewsProvider {

    public static final String AUTHORITY = "com.udacity.norbi930523.manutdapp.news";

    @TableEndpoint(table = ManUtdDatabase.ARTICLE)
    public static class News {

        @ContentUri(
                path = "news",
                type = "vnd.android.cursor.dir/list",
                defaultSort = ArticleColumns.DATE + " DESC")
        public static final Uri NEWS = Uri.parse("content://" + AUTHORITY + "/news");

        @InexactContentUri(
                path = "news/#",
                name = "ARTICLE_ID",
                type = "vnd.android.cursor.item/list",
                whereColumn = ArticleColumns._ID,
                pathSegment = 1)
        public static Uri withId(Long articleId) {
            return Uri.parse("content://" + AUTHORITY + "/news/" + articleId);
        }
    }

}
