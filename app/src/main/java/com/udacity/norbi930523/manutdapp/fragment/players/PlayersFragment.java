package com.udacity.norbi930523.manutdapp.fragment.players;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.activity.players.PlayerDetailsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayersFragment extends Fragment implements PlayerListFragment.PlayerListItemClickListener {

    private static final String SELECT_PLAYER_TEXT_VISIBILITY_KEY = "selectPlayerTextVisibility";

    @Nullable
    @BindView(R.id.selectPlayerText)
    TextView selectPlayerText;

    private boolean isTwoPaneLayout;

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
        View root = inflater.inflate(R.layout.fragment_players, container, false);

        ButterKnife.bind(this, root);

        if(selectPlayerText != null){
            isTwoPaneLayout = true;

            if(savedInstanceState != null){
                selectPlayerText.setVisibility(savedInstanceState.getInt(SELECT_PLAYER_TEXT_VISIBILITY_KEY));
            }
        } else {
            isTwoPaneLayout = false;
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(selectPlayerText != null){
            outState.putInt(SELECT_PLAYER_TEXT_VISIBILITY_KEY, selectPlayerText.getVisibility());
        }
    }

    @Override
    public void onPlayerClick(Long playerId, View sharedElement) {
        if(isTwoPaneLayout){
            selectPlayerText.setVisibility(View.GONE);
            swapPlayerDetailsFragment(playerId);
        } else {
            startPlayerDetailsActivity(playerId, sharedElement);
        }
    }

    private void swapPlayerDetailsFragment(Long playerId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        PlayerDetailsFragment pdf = PlayerDetailsFragment.newInstance(playerId);

        fragmentManager.beginTransaction()
                .replace(R.id.playerDetailsFragmentContainer, pdf)
                .commit();
    }

    private void startPlayerDetailsActivity(Long playerId, View sharedElement){
        Intent playerDetailsIntent = new Intent(getContext(), PlayerDetailsActivity.class);
        playerDetailsIntent.putExtra(PlayerDetailsActivity.PLAYER_ID_PARAM, playerId);

        /* https://guides.codepath.com/android/shared-element-activity-transition */
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), sharedElement, getString(R.string.player_image_transition));

        startActivity(playerDetailsIntent, options.toBundle());
    }
}
