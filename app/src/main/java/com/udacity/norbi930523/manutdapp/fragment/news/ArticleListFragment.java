package com.udacity.norbi930523.manutdapp.fragment.news;


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
import com.udacity.norbi930523.manutdapp.loader.NewsLoader;
import com.udacity.norbi930523.manutdapp.service.DataLoaderIntentService;
import com.udacity.norbi930523.manutdapp.ui.NewsRecyclerViewAdapter;
import com.udacity.norbi930523.manutdapp.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LIST_STATE_KEY = "listState";

    private static final String SELECTED_ITEM_INDEX_KEY = "selectedItemIndex";

    private static final int NEWS_LOADER_ID = 100;

    @BindView(R.id.articleListContainer)
    FrameLayout articleListContainer;

    @BindView(R.id.loadingIndicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.newsRecyclerView)
    RecyclerView newsRecyclerView;

    private NewsRecyclerViewAdapter newsAdapter;

    private Parcelable listState;

    public ArticleListFragment() {
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

        if(parent instanceof ArticleListItemClickListener){
            newsAdapter = new NewsRecyclerViewAdapter(context, (ArticleListItemClickListener) parent);
        } else {
            throw new RuntimeException("Parent must implement ArticleListItemClickListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_article_list, container, false);

        ButterKnife.bind(this, root);

        if(savedInstanceState != null){
            newsAdapter.setSelectedItemIndex(savedInstanceState.getInt(SELECTED_ITEM_INDEX_KEY));
        }

        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadNews(savedInstanceState == null);

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LIST_STATE_KEY, newsRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt(SELECTED_ITEM_INDEX_KEY, newsAdapter.getSelectedItemIndex());
    }

    private void loadNews(boolean initLoader){
        if(initLoader){
            /* On the first run, load articles from the server if online,
             * otherwise load from database */
            if(NetworkUtils.isOnline(getContext())){
                getContext().registerReceiver(loadingStatusChangeReceiver, new IntentFilter(DataLoaderIntentService.BROADCAST_ACTION_STATUS_CHANGE));

                DataLoaderIntentService.startActionLoadNews(getContext());
            } else {
                getActivity().getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

                Snackbar.make(articleListContainer, R.string.is_offline, Snackbar.LENGTH_LONG).show();
            }

        } else {
            /* This is not the first run, load articles from database */
            getActivity().getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        }

    }

    private BroadcastReceiver loadingStatusChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DataLoaderIntentService.BROADCAST_ACTION_STATUS_CHANGE.equals(intent.getAction())){
                int syncStatus = intent.getIntExtra(DataLoaderIntentService.BROADCAST_EXTRA_SYNC_STATUS, 0);

                toggleLoadingIndicator(syncStatus == DataLoaderIntentService.DataSyncStatus.IN_PROGRESS);

                if(syncStatus != DataLoaderIntentService.DataSyncStatus.IN_PROGRESS){
                    /* Finished loading data from server, init cursor loader */
                    getActivity().getSupportLoaderManager()
                            .initLoader(NEWS_LOADER_ID, null, ArticleListFragment.this);

                    /* Unregister the receiver when finished loading data,
                     * as we won't need it anymore while the user is on this screen */
                    getContext().unregisterReceiver(this);

                    if(syncStatus == DataLoaderIntentService.DataSyncStatus.SERVER_UNAVAILABLE){
                        Snackbar.make(articleListContainer, R.string.server_unavailable, Snackbar.LENGTH_LONG).show();
                    }
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
        return NewsLoader.allNews(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        newsAdapter.swapCursor(data);

        /* Restore saved state if there is any */
        if(listState != null){
            newsRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            listState = null;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface ArticleListItemClickListener {

        void onArticleClick(Long articleId, View sharedElement);

    }
}
