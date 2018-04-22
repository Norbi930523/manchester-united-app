package com.udacity.norbi930523.manutdapp.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.ProgressBar;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.loader.NewsLoader;
import com.udacity.norbi930523.manutdapp.network.DataLoaderIntentService;
import com.udacity.norbi930523.manutdapp.ui.NewsRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NEWS_LOADER_ID = 100;

    @BindView(R.id.loadingIndicator)
    ProgressBar loadingIndicator;

    @BindView(R.id.newsRecyclerView)
    RecyclerView newsRecyclerView;

    private NewsRecyclerViewAdapter newsAdapter;

    public ArticleListFragment() {
        // Required empty public constructor
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

        loadNews();

        return root;
    }

    private void loadNews(){
        loadingIndicator.setVisibility(View.VISIBLE);

        getActivity().getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

        DataLoaderIntentService.startActionLoadNews(getContext());
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
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface ArticleListItemClickListener {

        void onArticleClick(Long articleId, View sharedElement);

    }
}
