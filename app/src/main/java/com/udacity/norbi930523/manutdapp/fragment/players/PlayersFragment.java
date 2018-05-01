package com.udacity.norbi930523.manutdapp.fragment.players;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.activity.players.PlayerDetailsActivity;

public class PlayersFragment extends Fragment implements PlayerListFragment.PlayerListItemClickListener {

    public PlayersFragment() {
        // Required empty public constructor
    }

    public static PlayersFragment newInstance() {
        return new PlayersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_players);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_players, container, false);
    }

    @Override
    public void onPlayerClick(Long playerId, View sharedElement) {
        Intent playerDetailsIntent = new Intent(getContext(), PlayerDetailsActivity.class);
        playerDetailsIntent.putExtra(PlayerDetailsActivity.PLAYER_ID_PARAM, playerId);

        /* https://guides.codepath.com/android/shared-element-activity-transition */
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), sharedElement, getString(R.string.player_image_transition));

        startActivity(playerDetailsIntent, options.toBundle());
    }
}
