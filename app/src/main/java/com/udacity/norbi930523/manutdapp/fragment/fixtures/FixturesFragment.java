package com.udacity.norbi930523.manutdapp.fragment.fixtures;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixturesProvider;
import com.udacity.norbi930523.manutdapp.service.CalendarSyncIntentService;
import com.udacity.norbi930523.manutdapp.service.DataLoaderIntentService;
import com.udacity.norbi930523.manutdapp.util.DateUtils;
import com.udacity.norbi930523.manutdapp.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.internal.Utils.arrayOf;

public class FixturesFragment extends Fragment {

    public static final int PERMISSION_REQUEST_WRITE_CALENDAR = 0;

    @BindView(R.id.fixturesContainer)
    FrameLayout fixturesContainer;

    @BindView(R.id.fixturesPager)
    ViewPager fixturesPager;

    @BindView(R.id.loadingIndicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.syncButton)
    FloatingActionButton syncButton;

    private List<Long> fixtureMonths;

    public FixturesFragment() {
        // Required empty public constructor
    }

    public static FixturesFragment newInstance() {
        FixturesFragment fragment = new FixturesFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_fixtures);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter loadingIntentFilter = new IntentFilter();
        loadingIntentFilter.addAction(DataLoaderIntentService.BROADCAST_ACTION_STATE_CHANGE);
        loadingIntentFilter.addAction(CalendarSyncIntentService.BROADCAST_ACTION_STATE_CHANGE);

        getContext().registerReceiver(loadingStateChangeReceiver, loadingIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().unregisterReceiver(loadingStateChangeReceiver);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fixtures, container, false);

        ButterKnife.bind(this, root);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncFixturesToCalendar();
            }
        });

        loadFixtures(savedInstanceState == null);

        return root;
    }

    private void loadFixtures(boolean initLoader){
        if(initLoader){
            if(NetworkUtils.isOnline(getContext())){
                DataLoaderIntentService.startActionLoadFixtures(getContext());
            } else {
                Snackbar.make(fixturesContainer, R.string.is_offline, Snackbar.LENGTH_LONG).show();
            }

        }

    }

    public void syncFixturesToCalendar(){
        int permission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR);

        /* https://developer.android.com/training/permissions/requesting#java */
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),
                    arrayOf(Manifest.permission.WRITE_CALENDAR),
                    PERMISSION_REQUEST_WRITE_CALENDAR);
        } else {
            CalendarSyncIntentService.startActionSyncFixtures(getContext());
        }
    }

    private void populateFixturePages() {
        List<Long> fixtureDates = getFixtureDates();

        fixtureMonths = getDistinctFixtureMonths(fixtureDates);

        fixturesPager.setAdapter(new FixturesPagerAdapter(getActivity().getSupportFragmentManager()));

        Long currentFixtureMonthStart = getStartOfCurrentFixtureMonth(fixtureDates);
        int currentFixtureMonthStartIndex = fixtureMonths.contains(currentFixtureMonthStart) ? fixtureMonths.indexOf(currentFixtureMonthStart) : 0;

        fixturesPager.setCurrentItem(currentFixtureMonthStartIndex);
    }

    private Long getStartOfCurrentFixtureMonth(List<Long> fixtureDates) {
        Long now = System.currentTimeMillis();

        Long previousFixtureMillis = null;
        Long nextFixtureMillis = null;

        for(Long fixtureDate : fixtureDates){
            if(now >= fixtureDate){
                previousFixtureMillis = fixtureDate;
            } else {
                nextFixtureMillis = fixtureDate;
                break;
            }
        }

        if(previousFixtureMillis == null && nextFixtureMillis == null){
            return 0L; // empty fixtureDates list, should not happen
        } else if(previousFixtureMillis == null){
            /* Every fixture is in the future, show the next fixture month */
            return DateUtils.getStartOfMonth(nextFixtureMillis);
        } else if(nextFixtureMillis == null) {
            /* Every fixture is in the past, show the last fixture month */
            return DateUtils.getStartOfMonth(previousFixtureMillis);
        } else {
            /* Between two fixtures, show the current month */
            return DateUtils.getStartOfMonth(now);
        }

    }

    private List<Long> getFixtureDates(){
        Cursor cursor = getActivity().getContentResolver().query(
                FixturesProvider.Fixtures.FIXTURES,
                new String[]{ FixtureColumns.DATE },
                null,
                null,
                null
        );

        List<Long> fixtureDates = new ArrayList<>();

        while(cursor.moveToNext()){
            Long fixtureDateMillis = cursor.getLong(cursor.getColumnIndex(FixtureColumns.DATE));

            fixtureDates.add(fixtureDateMillis);
        }

        return fixtureDates;
    }

    private List<Long> getDistinctFixtureMonths(List<Long> fixtureDates){
        List<Long> distinctFixtureMonths = new ArrayList<>();

        for(Long fixtureDate : fixtureDates){
            Long startOfMonth = DateUtils.getStartOfMonth(fixtureDate);

            if(!distinctFixtureMonths.contains(startOfMonth)){
                distinctFixtureMonths.add(startOfMonth);
            }
        }

        return distinctFixtureMonths;
    }

    private BroadcastReceiver loadingStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DataLoaderIntentService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())){
                boolean isLoading = intent.getBooleanExtra(DataLoaderIntentService.BROADCAST_EXTRA_IS_LOADING, false);

                toggleLoadingIndicator(isLoading);

                if(!isLoading){
                    /* Populate fixture pages when finished loading */
                    populateFixturePages();
                }
            } else if (CalendarSyncIntentService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())){
                boolean isSyncing = intent.getBooleanExtra(CalendarSyncIntentService.BROADCAST_EXTRA_IS_SYNCING, false);

                int message = isSyncing ? R.string.fixtures_sync_start : R.string.fixtures_sync_end;

                Snackbar.make(fixturesContainer, message, Snackbar.LENGTH_LONG).show();
            }
        }

        private void toggleLoadingIndicator(final boolean isLoading){
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    };

    private class FixturesPagerAdapter extends FragmentStatePagerAdapter {
        FixturesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FixturesPageFragment.newInstance(fixtureMonths.get(position));
        }

        @Override
        public int getCount() {
            return fixtureMonths.size();
        }
    }

}
