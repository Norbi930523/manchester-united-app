package com.udacity.norbi930523.manutdapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.fragment.news.NewsFragment;
import com.udacity.norbi930523.manutdapp.fragment.players.PlayersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_MENU_ID_KEY = "selectedMenuId";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private int selectedMenuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        initSelectedMenu(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SELECTED_MENU_ID_KEY, selectedMenuId);
    }

    private void initSelectedMenu(Bundle savedInstanceState){
        if(savedInstanceState == null){
            selectedMenuId = R.id.navigation_news;
        } else {
            selectedMenuId = savedInstanceState.getInt(SELECTED_MENU_ID_KEY);
        }

        navigation.setSelectedItemId(selectedMenuId);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                case R.id.navigation_players:
                case R.id.navigation_fixtures:
                    changeView(item.getItemId());
                    return true;
            }
            return false;
        }
    };

    private void changeView(int selectedMenuId){
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.contentContainer, getFragmentBySelectedMenu(selectedMenuId))
                .commit();

        this.selectedMenuId = selectedMenuId;
    }

    private Fragment getFragmentBySelectedMenu(int selectedMenuId){
        switch (selectedMenuId){
            case R.id.navigation_news:
                return NewsFragment.newInstance();
            case R.id.navigation_players:
                return PlayersFragment.newInstance();
            case R.id.navigation_fixtures:
                return null;
        }

        throw new IllegalArgumentException("Unhandled menu item!");
    }

}
