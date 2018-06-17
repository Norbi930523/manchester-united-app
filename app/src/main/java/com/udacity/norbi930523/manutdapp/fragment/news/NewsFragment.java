package com.udacity.norbi930523.manutdapp.fragment.news;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.activity.news.ArticleDetailsActivity;
import com.udacity.norbi930523.manutdapp.util.AnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements ArticleListFragment.ArticleListItemClickListener {

    private static final String SELECT_ARTICLE_TEXT_VISIBILITY_KEY = "selectArticleTextVisibility";

    @Nullable
    @BindView(R.id.selectArticleText)
    TextView selectArticleText;

    private boolean isTwoPaneLayout;

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

        AnalyticsUtils.logScreenView(getActivity().getTitle().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.bind(this, root);

        if(selectArticleText != null){
            isTwoPaneLayout = true;

            if(savedInstanceState != null){
                selectArticleText.setVisibility(savedInstanceState.getInt(SELECT_ARTICLE_TEXT_VISIBILITY_KEY));
            }
        } else {
            isTwoPaneLayout = false;
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(selectArticleText != null){
            outState.putInt(SELECT_ARTICLE_TEXT_VISIBILITY_KEY, selectArticleText.getVisibility());
        }
    }

    @Override
    public void onArticleClick(Long articleId, View sharedElement) {
        if(isTwoPaneLayout){
            selectArticleText.setVisibility(View.GONE);
            swapArticleDetailsFragment(articleId);
        } else {
            startArticleDetailsActivity(articleId, sharedElement);
        }

    }

    private void swapArticleDetailsFragment(Long articleId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        ArticleDetailsFragment adf = ArticleDetailsFragment.newInstance(articleId);

        fragmentManager.beginTransaction()
                .replace(R.id.articleDetailsFragmentContainer, adf)
                .commit();
    }

    private void startArticleDetailsActivity(Long articleId, View sharedElement){
        Intent articleDetailsIntent = new Intent(getContext(), ArticleDetailsActivity.class);
        articleDetailsIntent.putExtra(ArticleDetailsActivity.ARTICLE_ID_PARAM, articleId);

        /* https://guides.codepath.com/android/shared-element-activity-transition */
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), sharedElement, ViewCompat.getTransitionName(sharedElement));

        startActivity(articleDetailsIntent, options.toBundle());
    }
}
