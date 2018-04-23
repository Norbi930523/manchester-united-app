package com.udacity.norbi930523.manutdapp.fragment;


import android.content.Context;
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
import com.udacity.norbi930523.manutdapp.network.DataLoaderIntentService;
import com.udacity.norbi930523.manutdapp.ui.NewsRecyclerViewAdapter;
import com.udacity.norbi930523.manutdapp.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LIST_STATE_KEY = "listState";

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

        if(context instanceof ArticleListItemClickListener){
            newsAdapter = new NewsRecyclerViewAdapter(context, (ArticleListItemClickListener) context);
        } else {
            throw new RuntimeException("Parent activity must implement ArticleListItemClickListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_article_list, container, false);

        ButterKnife.bind(this, root);

        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadNews(savedInstanceState == null);

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(LIST_STATE_KEY, newsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    private void loadNews(boolean initLoader){
        loadingIndicator.setVisibility(View.VISIBLE);

        if(initLoader){
            getActivity().getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

            if(NetworkUtils.isOnline(getContext())){
                DataLoaderIntentService.startActionLoadNews(getContext());
            } else {
                Snackbar.make(articleListContainer, R.string.is_offline, Snackbar.LENGTH_LONG).show();
            }

        } else {
            getActivity().getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return NewsLoader.allNews(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        loadingIndicator.setVisibility(View.GONE);

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
