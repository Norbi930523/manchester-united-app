package com.udacity.norbi930523.manutdapp.fragment.players;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.database.players.PlayerColumns;
import com.udacity.norbi930523.manutdapp.loader.PlayersLoader;
import com.udacity.norbi930523.manutdapp.util.PlayerPositionUtils;
import com.udacity.norbi930523.manutdapp.util.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PLAYER_LOADER_ID = 201;

    private static final String PLAYER_ID_PARAM = "playerId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.playerImage)
    ImageView playerImage;

    @BindView(R.id.squadNumber)
    TextView squadNumber;

    @BindView(R.id.birthdate)
    TextView birthdate;

    @BindView(R.id.position)
    TextView position;

    @BindView(R.id.joined)
    TextView joined;

    @BindView(R.id.international)
    TextView international;

    @BindView(R.id.appearances)
    TextView appearances;

    @BindView(R.id.goals)
    TextView goals;

    @BindView(R.id.bio)
    TextView bio;

    private Long playerId;

    public PlayerDetailsFragment() {
        // Required empty public constructor
    }

    public static PlayerDetailsFragment newInstance(Long playerId) {
        PlayerDetailsFragment fragment = new PlayerDetailsFragment();

        Bundle args = new Bundle();
        args.putLong(PLAYER_ID_PARAM, playerId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            playerId = getArguments().getLong(PLAYER_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player_details, container, false);

        ButterKnife.bind(this, root);

        if(savedInstanceState == null){
            getActivity().getSupportLoaderManager().initLoader(PLAYER_LOADER_ID, null, this);
        } else {
            getActivity().getSupportLoaderManager().restartLoader(PLAYER_LOADER_ID, null, this);
        }

        return root;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return PlayersLoader.singlePlayer(getContext(), playerId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToNext()){
            String lastName = cursor.getString(cursor.getColumnIndex(PlayerColumns.LAST_NAME));
            String firstName = cursor.getString(cursor.getColumnIndex(PlayerColumns.FIRST_NAME));
            String imageUrl = cursor.getString(cursor.getColumnIndex(PlayerColumns.IMAGE_URL));
            int squadNumber = cursor.getInt(cursor.getColumnIndex(PlayerColumns.SQUAD_NUMBER));
            String birthdate = cursor.getString(cursor.getColumnIndex(PlayerColumns.BIRTHDATE));
            String birthplace = cursor.getString(cursor.getColumnIndex(PlayerColumns.BIRTHPLACE));
            String joined = cursor.getString(cursor.getColumnIndex(PlayerColumns.JOINED));
            String joinedFrom = cursor.getString(cursor.getColumnIndex(PlayerColumns.JOINED_FROM));
            String international = cursor.getString(cursor.getColumnIndex(PlayerColumns.INTERNATIONAL));
            int appearances = cursor.getInt(cursor.getColumnIndex(PlayerColumns.APPEARANCES));
            int goals = cursor.getInt(cursor.getColumnIndex(PlayerColumns.GOALS));
            String bio = cursor.getString(cursor.getColumnIndex(PlayerColumns.BIO));

            int positionId = cursor.getInt(cursor.getColumnIndex(PlayerColumns.POSITION));
            String position = PlayerPositionUtils.toString(getContext(), positionId);

            initToolbar(firstName + " " + lastName);

            loadImage(imageUrl);

            TextUtils.setHtmlText(this.squadNumber, getString(R.string.player_info_squad_number, squadNumber));
            TextUtils.setHtmlText(this.birthdate, getString(R.string.player_info_birthdate, birthdate, birthplace));
            TextUtils.setHtmlText(this.position, getString(R.string.player_info_position, position));
            TextUtils.setHtmlText(this.joined, getString(R.string.player_info_joined, joined, joinedFrom));
            TextUtils.setHtmlText(this.international, getString(R.string.player_info_international, international));
            TextUtils.setHtmlText(this.appearances, getString(R.string.player_info_appearances, appearances));
            TextUtils.setHtmlText(this.goals, getString(R.string.player_info_goals, goals));
            TextUtils.setHtmlText(this.bio, bio);

        } else {
            Toast.makeText(getContext(), R.string.player_not_found, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void initToolbar(String title){
        toolbar.setTitle(title);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadImage(String imageUrl){
        /* Start transition when the ImageView is laid out */
        playerImage.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        playerImage.getViewTreeObserver().removeOnPreDrawListener(this);
                        getActivity().supportStartPostponedEnterTransition();
                        return true;
                    }
                }
        );

        Picasso.with(getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(playerImage);
    }
}
