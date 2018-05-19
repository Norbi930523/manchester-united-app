package com.udacity.norbi930523.manutdapp.fragment.fixtures;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixturesProvider;
import com.udacity.norbi930523.manutdapp.network.DataLoaderIntentService;
import com.udacity.norbi930523.manutdapp.util.DateUtils;
import com.udacity.norbi930523.manutdapp.util.NetworkUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FixturesFragment extends Fragment {

    @BindView(R.id.fixturesContainer)
    FrameLayout fixturesContainer;

    @BindView(R.id.fixturesPager)
    ViewPager fixturesPager;

    @BindView(R.id.loadingIndicator)
    ProgressBar loadingIndicator;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fixtures, container, false);

        ButterKnife.bind(this, root);

        loadFixtures(savedInstanceState == null);

        return root;
    }

    private void loadFixtures(boolean initLoader){
        if(initLoader){
            if(NetworkUtils.isOnline(getContext())){
                getContext().registerReceiver(loadingStateChangeReceiver, new IntentFilter(DataLoaderIntentService.BROADCAST_ACTION_STATE_CHANGE));

                DataLoaderIntentService.startActionLoadFixtures(getContext());
            } else {
                Snackbar.make(fixturesContainer, R.string.is_offline, Snackbar.LENGTH_LONG).show();
            }

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
                    /* Unregister the receiver when finished loading data */
                    getContext().unregisterReceiver(this);

                    populateFixturePages();
                }
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
