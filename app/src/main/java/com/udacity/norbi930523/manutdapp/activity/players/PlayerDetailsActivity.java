package com.udacity.norbi930523.manutdapp.activity.players;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.fragment.news.ArticleDetailsFragment;
import com.udacity.norbi930523.manutdapp.fragment.players.PlayerDetailsFragment;

public class PlayerDetailsActivity extends AppCompatActivity {

    public static final String PLAYER_ID_PARAM = "playerId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        /* Wait until article image is loaded */
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        Long playerId = extras.getLong(PLAYER_ID_PARAM);

        if(savedInstanceState == null){
            showPlayer(playerId);
        }
    }

    private void showPlayer(Long playerId){
        PlayerDetailsFragment adf = PlayerDetailsFragment.newInstance(playerId);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.playerDetailsFragmentContainer, adf)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportFinishAfterTransition();
        return true;
    }
}
