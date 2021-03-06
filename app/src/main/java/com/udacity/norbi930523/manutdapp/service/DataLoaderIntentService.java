package com.udacity.norbi930523.manutdapp.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.google.api.client.util.DateTime;
import com.udacity.norbi930523.manutdapp.backend.manutd.model.ArticleVO;
import com.udacity.norbi930523.manutdapp.backend.manutd.model.FixtureVO;
import com.udacity.norbi930523.manutdapp.backend.manutd.model.PlayerVO;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixturesProvider;
import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.database.news.NewsProvider;
import com.udacity.norbi930523.manutdapp.database.players.PlayerColumns;
import com.udacity.norbi930523.manutdapp.database.players.PlayersProvider;
import com.udacity.norbi930523.manutdapp.util.ApiUtils;
import com.udacity.norbi930523.manutdapp.util.DateUtils;
import com.udacity.norbi930523.manutdapp.util.NetworkUtils;
import com.udacity.norbi930523.manutdapp.util.PlayerPositionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import timber.log.Timber;


public class DataLoaderIntentService extends IntentService {

    public static final String BROADCAST_ACTION_STATUS_CHANGE = "DataLoaderIntentService.STATUS_CHANGE";
    public static final String BROADCAST_EXTRA_SYNC_STATUS = "DataLoaderIntentService.SYNC_STATUS";

    public static class DataSyncStatus {
        public static final int IN_PROGRESS = 0;
        public static final int SUCCESS = 1;
        public static final int SERVER_UNAVAILABLE = 2;
    }

    private static final String NEWS_LAST_UPDATE_KEY = "newsLastUpdate";

    private static final String ACTION_LOAD_NEWS = "com.udacity.norbi930523.manutdapp.network.action.LOAD_NEWS";
    private static final String ACTION_LOAD_PLAYERS = "com.udacity.norbi930523.manutdapp.network.action.LOAD_PLAYERS";
    private static final String ACTION_LOAD_FIXTURES = "com.udacity.norbi930523.manutdapp.network.action.LOAD_FIXTURES";

    public DataLoaderIntentService() {
        super("DataLoaderIntentService");
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

        broadcastStatusChange(DataSyncStatus.IN_PROGRESS);

        List<ArticleVO> articles = loadArticlesFromServer();

        if(articles == null){
            broadcastStatusChange(DataSyncStatus.SERVER_UNAVAILABLE);
            return;
        }

        updateNewsLastUpdate();

        ContentValues[] values = new ContentValues[articles.size()];
        for(int i = 0; i < articles.size(); i++){
            ArticleVO article = articles.get(i);

            ContentValues cv = new ContentValues();
            cv.put(ArticleColumns._ID, article.getId());
            cv.put(ArticleColumns.TITLE, article.getTitle());
            cv.put(ArticleColumns.AUTHOR, article.getAuthor());
            cv.put(ArticleColumns.DATE, DateUtils.parseDate(article.getDate()));
            cv.put(ArticleColumns.SUMMARY, article.getSummary());
            cv.put(ArticleColumns.CONTENT, article.getContent());
            cv.put(ArticleColumns.IMAGE_URL, article.getImageUrl());

            values[i] = cv;
        }

        getContentResolver().bulkInsert(NewsProvider.News.NEWS, values);

        broadcastStatusChange(DataSyncStatus.SUCCESS);
    }

    private List<ArticleVO> loadArticlesFromServer(){
        if(NetworkUtils.isOffline(this)){
            return null;
        }

        try {
            return ApiUtils.getManutdApiService().news(getNewsLastUpdate()).execute().getItems();
        } catch (IOException e) {
            Timber.e(e, "Failed to load news from server");
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

        broadcastStatusChange(DataSyncStatus.IN_PROGRESS);

        List<PlayerVO> players = loadPlayersFromServer();

        if(players == null){
            broadcastStatusChange(DataSyncStatus.SERVER_UNAVAILABLE);
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

        broadcastStatusChange(DataSyncStatus.SUCCESS);
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
            return ApiUtils.getManutdApiService().players().execute().getItems();
        } catch (IOException e) {
            Timber.e(e, "Failed to load players from server");
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

        broadcastStatusChange(DataSyncStatus.IN_PROGRESS);

        List<FixtureVO> fixtures = loadFixturesFromServer();

        if(fixtures == null){
            broadcastStatusChange(DataSyncStatus.SERVER_UNAVAILABLE);
            return;
        }

        Date now = new Date();

        for(int i = 0; i < fixtures.size(); i++){
            FixtureVO fixture = fixtures.get(i);

            ContentValues cv = new ContentValues();
            cv.put(FixtureColumns._ID, fixture.getId());
            cv.put(FixtureColumns.COMPETITION, fixture.getCompetition());
            cv.put(FixtureColumns.DATE, DateUtils.parseDate(fixture.getDate()));
            cv.put(FixtureColumns.OPPONENT, fixture.getOpponent());
            cv.put(FixtureColumns.VENUE, fixture.getVenue());
            cv.put(FixtureColumns.RESULT, fixture.getResult());
            cv.put(FixtureColumns.LAST_UPDATE, now.getTime());

            if(isExistingFixture(fixture.getId())){
                getContentResolver().update(FixturesProvider.Fixtures.withId(fixture.getId()), cv, null, null);
            } else {
                getContentResolver().insert(FixturesProvider.Fixtures.FIXTURES, cv);
            }
        }

        /* Delete fixtures that were not present in the json, based on their last update time */
        deleteMissingFixtures(now.getTime());

        broadcastStatusChange(DataSyncStatus.SUCCESS);

        /* Update the widget when fixtures change */
        NextMatchWidgetUpdateService.startActionUpdateNextMatch(this);
    }

    private List<FixtureVO> loadFixturesFromServer(){
        if(NetworkUtils.isOffline(this)){
            return null;
        }

        try {
            return ApiUtils.getManutdApiService().fixtures().execute().getItems();
        } catch (IOException e) {
            Timber.e(e, "Failed to load fixtures from server");
            return null;
        }
    }

    private boolean isExistingFixture(String fixtureId){
        Cursor cursor = getContentResolver().query(
                FixturesProvider.Fixtures.withId(fixtureId),
                new String[]{ FixtureColumns._ID },
                null,
                null,
                null
        );

        return cursor != null && cursor.getCount() > 0;
    }

    private void deleteMissingFixtures(long nowInMillis) {
        getContentResolver().delete(
                FixturesProvider.Fixtures.FIXTURES,
                FixtureColumns.LAST_UPDATE + " < ?",
                new String[]{ String.valueOf(nowInMillis) }
        );
    }

    private void broadcastStatusChange(int syncStatus){
        Intent statusChange = new Intent(BROADCAST_ACTION_STATUS_CHANGE);
        statusChange.putExtra(BROADCAST_EXTRA_SYNC_STATUS, syncStatus);

        sendBroadcast(statusChange);
    }
}
