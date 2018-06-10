package com.udacity.norbi930523.manutdapp.fragment.fixtures;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.loader.FixturesLoader;
import com.udacity.norbi930523.manutdapp.ui.FixturesRecyclerViewAdapter;
import com.udacity.norbi930523.manutdapp.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FixturesPageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FIXTURES_LOADER_ID = 300;

    private static final String LIST_STATE_KEY = "listState";

    private static final String START_OF_MONTH_PARAM = "startOfMonth";

    @BindView(R.id.fixtureMonth)
    TextView fixtureMonth;

    @BindView(R.id.fixturesRecyclerView)
    RecyclerView fixturesRecyclerView;

    private FixturesRecyclerViewAdapter fixturesAdapter;

    private Integer monthIndex;

    private long startOfMonth;

    private long endOfMonth;

    private Parcelable listState;

    public FixturesPageFragment() {
        // Required empty public constructor
    }


    static FixturesPageFragment newInstance(Long startOfMonth) {
        FixturesPageFragment fragment = new FixturesPageFragment();

        Bundle args = new Bundle();
        args.putLong(START_OF_MONTH_PARAM, startOfMonth);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            startOfMonth = args.getLong(START_OF_MONTH_PARAM);
            endOfMonth = DateUtils.getEndOfMonth(startOfMonth);
            monthIndex = DateUtils.getMonth(startOfMonth);
        }

        if(savedInstanceState != null){
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        fixturesAdapter = new FixturesRecyclerViewAdapter(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fixtures_page, container, false);

        ButterKnife.bind(this, root);

        String monthName = getResources().getStringArray(R.array.months)[monthIndex];
        fixtureMonth.setText(monthName);

        fixturesRecyclerView.setAdapter(fixturesAdapter);
        fixturesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFixtures(savedInstanceState == null);

        return root;
    }

    private void loadFixtures(boolean initLoader){
        if(initLoader){
            getActivity().getSupportLoaderManager().initLoader(getLoaderId(), null, this);
        } else {
            getActivity().getSupportLoaderManager().restartLoader(getLoaderId(), null, this);
        }

    }

    private int getLoaderId(){
        return FIXTURES_LOADER_ID + monthIndex;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LIST_STATE_KEY, fixturesRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return FixturesLoader.fixturesByDateRange(getContext(), startOfMonth, endOfMonth);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        fixturesAdapter.swapCursor(data);

        /* Restore saved state if there is any */
        if(listState != null){
            fixturesRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            listState = null;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
