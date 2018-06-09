package com.udacity.norbi930523.manutdapp.fragment.players;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.loader.PlayersLoader;
import com.udacity.norbi930523.manutdapp.service.DataLoaderIntentService;
import com.udacity.norbi930523.manutdapp.ui.PlayersRecyclerViewAdapter;
import com.udacity.norbi930523.manutdapp.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LIST_STATE_KEY = "listState";

    private static final String SELECTED_ITEM_INDEX_KEY = "selectedItemIndex";

    private static final int PLAYERS_LOADER_ID = 200;

    @BindView(R.id.playerListContainer)
    FrameLayout playerListContainer;

    @BindView(R.id.loadingIndicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.playersRecyclerView)
    RecyclerView playersRecyclerView;

    private PlayersRecyclerViewAdapter playersAdapter;

    private Parcelable listState;

    public PlayerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Fragment parent = getParentFragment();

        if(parent instanceof PlayerListItemClickListener){
            playersAdapter = new PlayersRecyclerViewAdapter(context, (PlayerListItemClickListener) parent);
        } else {
            throw new RuntimeException("Parent must implement PlayerListItemClickListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_player_list, container, false);

        ButterKnife.bind(this, root);

        if(savedInstanceState != null){
            playersAdapter.setSelectedItemIndex(savedInstanceState.getInt(SELECTED_ITEM_INDEX_KEY));
        }

        playersRecyclerView.setAdapter(playersAdapter);
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPlayers(savedInstanceState == null);

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LIST_STATE_KEY, playersRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt(SELECTED_ITEM_INDEX_KEY, playersAdapter.getSelectedItemIndex());
    }

    private void loadPlayers(boolean initLoader){
        if(initLoader){
            /* On the first run, load players from the server if online,
             * otherwise load from database */
            if(NetworkUtils.isOnline(getContext())){
                getContext().registerReceiver(loadingStateChangeReceiver, new IntentFilter(DataLoaderIntentService.BROADCAST_ACTION_STATE_CHANGE));

                DataLoaderIntentService.startActionLoadPlayers(getContext());
            } else {
                getActivity().getSupportLoaderManager().initLoader(PLAYERS_LOADER_ID, null, this);

                Snackbar.make(playerListContainer, R.string.is_offline, Snackbar.LENGTH_LONG).show();
            }

        } else {
            /* This is not the first run, load players from database */
            getActivity().getSupportLoaderManager().restartLoader(PLAYERS_LOADER_ID, null, this);
        }

    }

    private BroadcastReceiver loadingStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DataLoaderIntentService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())){
                boolean isLoading = intent.getBooleanExtra(DataLoaderIntentService.BROADCAST_EXTRA_IS_LOADING, false);

                toggleLoadingIndicator(isLoading);

                if(!isLoading){
                    /* Finished loading data from server, init cursor loader */
                    getActivity().getSupportLoaderManager()
                            .initLoader(PLAYERS_LOADER_ID, null, PlayerListFragment.this);

                    /* Unregister the receiver when finished loading data,
                     * as we won't need it anymore while the user is on this screen */
                    getContext().unregisterReceiver(this);
                }
            }
        }

        private void toggleLoadingIndicator(final boolean isLoading){
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return PlayersLoader.allPlayers(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        playersAdapter.swapCursor(data);

        /* Restore saved state if there is any */
        if(listState != null){
            playersRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            listState = null;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface PlayerListItemClickListener {

        void onPlayerClick(Long playerId, View sharedElement);

    }
}
