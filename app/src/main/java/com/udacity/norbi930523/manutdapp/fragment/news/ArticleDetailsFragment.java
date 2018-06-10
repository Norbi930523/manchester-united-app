package com.udacity.norbi930523.manutdapp.fragment.news;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.activity.MainActivity;
import com.udacity.norbi930523.manutdapp.activity.news.ArticleDetailsActivity;
import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.loader.NewsLoader;
import com.udacity.norbi930523.manutdapp.util.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ARTICLE_LOADER_ID = 101;

    private static final String ARTICLE_ID_PARAM = "articleId";
    
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.articleImage)
    ImageView articleImage;

    @BindView(R.id.articleSummary)
    TextView articleSummary;

    @BindView(R.id.articleContent)
    TextView articleContent;

    private Long articleId;

    public ArticleDetailsFragment() {
        // Required empty public constructor
    }

    public static ArticleDetailsFragment newInstance(Long articleId) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();

        Bundle args = new Bundle();
        args.putLong(ARTICLE_ID_PARAM, articleId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            articleId = getArguments().getLong(ARTICLE_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_article_details, container, false);

        ButterKnife.bind(this, root);
        
        ViewCompat.setTransitionName(articleImage, getString(R.string.article_image_transition, articleId));

        getActivity().getSupportLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);

        return root;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return NewsLoader.singleArticle(getContext(), articleId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(ArticleColumns.TITLE));
            String imageUrl = cursor.getString(cursor.getColumnIndex(ArticleColumns.IMAGE_URL));
            String summary = cursor.getString(cursor.getColumnIndex(ArticleColumns.SUMMARY));
            String content = cursor.getString(cursor.getColumnIndex(ArticleColumns.CONTENT));

            initToolbar(title);

            loadImage(imageUrl);

            articleSummary.setText(summary);

            TextUtils.setHtmlText(articleContent, content);

        } else {
            Toast.makeText(getContext(), R.string.article_not_found, Toast.LENGTH_LONG).show();

            if(getActivity() instanceof ArticleDetailsActivity){
                getActivity().finish();
            }
        }

        getActivity().getSupportLoaderManager().destroyLoader(ARTICLE_LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void initToolbar(String title){
        toolbar.setTitle(title);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(activity instanceof ArticleDetailsActivity){
            /* Change toolbar only in single pane layout */
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void loadImage(String imageUrl){
        Picasso.with(getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(articleImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        getActivity().supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        getActivity().supportStartPostponedEnterTransition();
                    }
                });
    }
}
