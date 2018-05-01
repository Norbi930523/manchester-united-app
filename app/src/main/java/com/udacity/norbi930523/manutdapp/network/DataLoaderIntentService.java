package com.udacity.norbi930523.manutdapp.network;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.udacity.norbi930523.manutdapp.backend.manutd.model.PlayerVO;
import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.database.news.NewsProvider;
import com.udacity.norbi930523.manutdapp.database.players.PlayerColumns;
import com.udacity.norbi930523.manutdapp.database.players.PlayersProvider;
import com.udacity.norbi930523.manutdapp.util.DateUtils;
import com.udacity.norbi930523.manutdapp.util.NetworkUtils;
import com.udacity.norbi930523.manutdapp.util.PlayerPositionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import timber.log.Timber;


public class DataLoaderIntentService extends IntentService {

    public static final String BROADCAST_ACTION_STATE_CHANGE = "DataLoaderIntentService.STATE_CHANGE";
    public static final String BROADCAST_EXTRA_IS_LOADING = "DataLoaderIntentService.IS_LOADING";

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

        broadcastStateChange(true);

        List<ArticleVO> articles = loadArticlesFromServer();

        if(articles == null){
            broadcastStateChange(false);
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

        broadcastStateChange(false);
    }

    private List<ArticleVO> loadArticlesFromServer(){
        if(NetworkUtils.isOffline(this)){
            return null;
        }

        try {
            return manutdApiService.news(getNewsLastUpdate()).execute().getItems();
        } catch (IOException e) {
            Timber.e(e, "Failed to load news from API");
            return null;
        }
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
        if(manutdApiService == null){
            initApiService();
        }

        broadcastStateChange(true);

        List<PlayerVO> players = loadPlayersFromServer();

        if(players == null){
            broadcastStateChange(false);
            return;
        }

        Date now = new Date();

        for(int i = 0; i < players.size(); i++){
            PlayerVO player = players.get(i);

            ContentValues cv = new ContentValues();
            cv.put(PlayerColumns._ID, player.getId());
            cv.put(PlayerColumns.APPEARANCES, player.getAppearances());
            cv.put(PlayerColumns.BIRTHDATE, player.getBirthdate());
            cv.put(PlayerColumns.BIRTHPLACE, player.getBirthplace());
            cv.put(PlayerColumns.FIRST_NAME, player.getFirstName());
            cv.put(PlayerColumns.GOALS, player.getGoals());
            cv.put(PlayerColumns.IMAGE_URL, player.getImageUrl());
            cv.put(PlayerColumns.INTERNATIONAL, player.getInternational());
            cv.put(PlayerColumns.JOINED, player.getJoined());
            cv.put(PlayerColumns.JOINED_FROM, player.getJoinedFrom());
            cv.put(PlayerColumns.LAST_NAME, player.getLastName());
            cv.put(PlayerColumns.POSITION, PlayerPositionUtils.valueOf(this, player.getPosition()));
            cv.put(PlayerColumns.SQUAD_NUMBER, player.getSquadNumber());
            cv.put(PlayerColumns.BIO, player.getBio());
            cv.put(PlayerColumns.LAST_UPDATE, now.getTime());

            if(isExistingPlayer(player.getId())){
                getContentResolver().update(PlayersProvider.Players.withId(player.getId()), cv, null, null);
            } else {
                getContentResolver().insert(PlayersProvider.Players.PLAYERS, cv);
            }
        }

        /* Delete players that were not present in the json, based on their last update time */
        deleteMissingPlayers(now.getTime());

        broadcastStateChange(false);
    }

    private void deleteMissingPlayers(long nowInMillis) {
        getContentResolver().delete(
                PlayersProvider.Players.PLAYERS,
                PlayerColumns.LAST_UPDATE + " < ?",
                new String[]{ String.valueOf(nowInMillis) }
        );
    }

    private List<PlayerVO> loadPlayersFromServer(){
        if(NetworkUtils.isOffline(this)){
            return null;
        }

        try {
            return manutdApiService.players().execute().getItems();
        } catch (IOException e) {
            Timber.e(e, "Failed to load players from API");
            return null;
        }
    }

    private boolean isExistingPlayer(Long playerId){
        Cursor cursor = getContentResolver().query(
                PlayersProvider.Players.withId(playerId),
                new String[]{ PlayerColumns._ID },
                null,
                null,
                null
        );

        return cursor != null && cursor.getCount() > 0;
    }

    private void handleActionLoadFixtures() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void broadcastStateChange(boolean isLoading){
        Intent stateChange = new Intent(BROADCAST_ACTION_STATE_CHANGE);
        stateChange.putExtra(BROADCAST_EXTRA_IS_LOADING, isLoading);

        sendBroadcast(stateChange);
    }
}
