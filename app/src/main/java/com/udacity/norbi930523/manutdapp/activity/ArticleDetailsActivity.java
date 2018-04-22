package com.udacity.norbi930523.manutdapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.fragment.ArticleDetailsFragment;

public class ArticleDetailsActivity extends AppCompatActivity {

    public static final String ARTICLE_ID_PARAM = "articleId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        /* Wait until article image is loaded */
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        Long articleId = extras.getLong(ARTICLE_ID_PARAM);

        showArticle(articleId);
    }

    private void showArticle(Long articleId){
        ArticleDetailsFragment adf = ArticleDetailsFragment.newInstance(articleId);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.articleDetailsFragmentContainer, adf)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        supportFinishAfterTransition();
        return true;
    }
}
