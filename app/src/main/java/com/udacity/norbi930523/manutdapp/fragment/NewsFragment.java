package com.udacity.norbi930523.manutdapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.activity.ArticleDetailsActivity;

public class NewsFragment extends Fragment implements ArticleListFragment.ArticleListItemClickListener {

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.title_news);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onArticleClick(Long articleId, View sharedElement) {
        Intent articleDetailsIntent = new Intent(getContext(), ArticleDetailsActivity.class);
        articleDetailsIntent.putExtra(ArticleDetailsActivity.ARTICLE_ID_PARAM, articleId);

        /* https://guides.codepath.com/android/shared-element-activity-transition */
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), sharedElement, getString(R.string.article_image_transition));

        startActivity(articleDetailsIntent, options.toBundle());
    }
}
