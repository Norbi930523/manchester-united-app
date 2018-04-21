package com.udacity.norbi930523.manutdapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.fragment.ArticleListFragment;

public class NewsActivity extends AppCompatActivity implements ArticleListFragment.ArticleListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    @Override
    public void onArticleClick(Long articleId) {
        Intent articleDetailsIntent = new Intent(this, ArticleDetailsActivity.class);
        articleDetailsIntent.putExtra(ArticleDetailsActivity.ARTICLE_ID_PARAM, articleId);

        startActivity(articleDetailsIntent);
    }
}
