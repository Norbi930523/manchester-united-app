package com.udacity.norbi930523.manutdapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.fragment.ArticleListFragment;

public class NewsActivity extends AppCompatActivity implements ArticleListFragment.ArticleListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    @Override
    public void onArticleClick(Long articleId, View sharedElement) {
        Intent articleDetailsIntent = new Intent(this, ArticleDetailsActivity.class);
        articleDetailsIntent.putExtra(ArticleDetailsActivity.ARTICLE_ID_PARAM, articleId);

        /* https://guides.codepath.com/android/shared-element-activity-transition */
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, sharedElement, getString(R.string.article_image_transition));

        startActivity(articleDetailsIntent, options.toBundle());
    }
}
