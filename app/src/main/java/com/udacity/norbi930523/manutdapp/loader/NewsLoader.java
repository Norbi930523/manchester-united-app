package com.udacity.norbi930523.manutdapp.loader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;

import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.database.news.NewsProvider;

public class NewsLoader extends CursorLoader {

    private static final String[] PROJECTION_DETAIL = {
            ArticleColumns._ID,
            ArticleColumns.TITLE,
            ArticleColumns.SUBTITLE,
            ArticleColumns.DATE,
            ArticleColumns.SUMMARY,
            ArticleColumns.CONTENT,
            ArticleColumns.IMAGE_URL
    };

    private static final String[] PROJECTION_LIST = {
            ArticleColumns._ID,
            ArticleColumns.TITLE,
            ArticleColumns.DATE,
            ArticleColumns.IMAGE_URL
    };

    public static NewsLoader allNews(Context context){
        return new NewsLoader(context, NewsProvider.News.NEWS, PROJECTION_LIST, null, null, null);
    }

    public static NewsLoader singleArticle(Context context, Long articleId){
        return new NewsLoader(context, NewsProvider.News.withId(articleId), PROJECTION_DETAIL, null, null, null);
    }

    private NewsLoader(@NonNull Context context, @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

}
