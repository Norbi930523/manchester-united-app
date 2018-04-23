package com.udacity.norbi930523.manutdapp.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;
import com.udacity.norbi930523.manutdapp.BuildConfig;
import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.backend.manutd.Manutd;
import com.udacity.norbi930523.manutdapp.backend.manutd.model.ArticleVO;
import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.database.news.NewsProvider;
import com.udacity.norbi930523.manutdapp.util.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import timber.log.Timber;


public class DataLoaderIntentService extends IntentService {

    private static final String NEWS_LAST_UPDATE_KEY = "newsLastUpdate";

    private static final String ACTION_LOAD_NEWS = "com.udacity.norbi930523.manutdapp.network.action.LOAD_NEWS";
    private static final String ACTION_LOAD_PLAYERS = "com.udacity.norbi930523.manutdapp.network.action.LOAD_PLAYERS";
    private static final String ACTION_LOAD_FIXTURES = "com.udacity.norbi930523.manutdapp.network.action.LOAD_FIXTURES";

    private Manutd manutdApiService;

    public DataLoaderIntentService() {
        super("DataLoaderIntentService");
    }

    private void initApiService(){
        Manutd.Builder builder = new Manutd.Builder(
                AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(),
                null);

        builder.setApplicationName(getString(R.string.app_name));

        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver
        builder.setRootUrl(BuildConfig.API_ENDPOINT)
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(BuildConfig.DEBUG);
                    }
                });
        // end options for devappserver

        manutdApiService = builder.build();
    }

    public static void startActionLoadNews(Context context) {
        Intent intent = new Intent(context, DataLoaderIntentService.class);
        intent.setAction(ACTION_LOAD_NEWS);
        context.startService(intent);
    }

    public static void startActionLoadPlayers(Context context) {
        Intent intent = new Intent(context, DataLoaderIntentService.class);
        intent.setAction(ACTION_LOAD_PLAYERS);
        context.startService(intent);
    }

    public static void startActionLoadFixtures(Context context) {
        Intent intent = new Intent(context, DataLoaderIntentService.class);
        intent.setAction(ACTION_LOAD_FIXTURES);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_LOAD_NEWS.equals(action)) {
                handleActionLoadNews();
            } else if (ACTION_LOAD_PLAYERS.equals(action)) {
                handleActionLoadPlayers();
            } else if (ACTION_LOAD_FIXTURES.equals(action)) {
                handleActionLoadFixtures();
            }
        }
    }

    private void handleActionLoadNews() {
        if(manutdApiService == null){
            initApiService();
        }

        List<ArticleVO> articles = null;
        try {
            articles = manutdApiService.news(getNewsLastUpdate()).execute().getItems();
        } catch (IOException e) {
            Timber.e(e, "Failed to load news from API");
            return;
        }

        updateNewsLastUpdate();

        ContentValues[] values = new ContentValues[articles.size()];
        for(int i = 0; i < articles.size(); i++){
            ArticleVO article = articles.get(i);

            ContentValues cv = new ContentValues();
            cv.put(ArticleColumns._ID, article.getId());
            cv.put(ArticleColumns.TITLE, article.getTitle());
            cv.put(ArticleColumns.SUBTITLE, article.getSubtitle());
            cv.put(ArticleColumns.DATE, DateUtils.parseDate(article.getDate()));
            cv.put(ArticleColumns.SUMMARY, article.getSummary());
            cv.put(ArticleColumns.CONTENT, article.getContent());
            cv.put(ArticleColumns.IMAGE_URL, article.getImageUrl());

            values[i] = cv;
        }

        getContentResolver().bulkInsert(NewsProvider.News.NEWS, values);
    }

    private DateTime getNewsLastUpdate(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        long lastUpdateMillis = sharedPreferences.getLong(NEWS_LAST_UPDATE_KEY, 0L);

        return new DateTime(lastUpdateMillis);
    }

    private void updateNewsLastUpdate(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Date now = new Date();

        sharedPreferences
                .edit()
                .putLong(NEWS_LAST_UPDATE_KEY, now.getTime())
                .apply();

    }

    private void handleActionLoadPlayers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionLoadFixtures() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
