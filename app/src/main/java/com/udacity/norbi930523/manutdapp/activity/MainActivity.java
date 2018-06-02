package com.udacity.norbi930523.manutdapp.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.fragment.fixtures.FixturesFragment;
import com.udacity.norbi930523.manutdapp.fragment.news.NewsFragment;
import com.udacity.norbi930523.manutdapp.fragment.players.PlayersFragment;
import com.udacity.norbi930523.manutdapp.service.CalendarSyncIntentService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if(savedInstanceState == null){
            navigation.setSelectedItemId(R.id.navigation_news);
        }

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
    }

    private Fragment getFragmentBySelectedMenu(int selectedMenuId){
        switch (selectedMenuId){
            case R.id.navigation_news:
                return NewsFragment.newInstance();
            case R.id.navigation_players:
                return PlayersFragment.newInstance();
            case R.id.navigation_fixtures:
                return FixturesFragment.newInstance();
        }

        throw new IllegalArgumentException("Unhandled menu item!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FixturesFragment.PERMISSION_REQUEST_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CalendarSyncIntentService.startActionSyncFixtures(this);
                }
            }

        }
    }

}
