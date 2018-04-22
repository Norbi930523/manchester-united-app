package com.udacity.norbi930523.manutdapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.database.news.ArticleColumns;
import com.udacity.norbi930523.manutdapp.fragment.ArticleListFragment;
import com.udacity.norbi930523.manutdapp.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ArticleViewHolder> {

    private Cursor cursor;

    private Context context;

    private ArticleListFragment.ArticleListItemClickListener articleListItemClickListener;

    public NewsRecyclerViewAdapter(Context context, ArticleListFragment.ArticleListItemClickListener articleListItemClickListener){
        super();
        this.context = context;
        this.articleListItemClickListener = articleListItemClickListener;

        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View articleListItem = layoutInflater.inflate(R.layout.article_list_item, parent, false);

        return new ArticleViewHolder(articleListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        cursor.moveToPosition(position);

        holder.articleId = cursor.getLong(cursor.getColumnIndex(ArticleColumns._ID));
        holder.articleTitle.setText(cursor.getString(cursor.getColumnIndex(ArticleColumns.TITLE)));

        long articleDate = cursor.getLong(cursor.getColumnIndex(ArticleColumns.DATE));
        holder.articleDate.setText(DateUtils.formatDate(articleDate));

        String imageUrl = cursor.getString(cursor.getColumnIndex(ArticleColumns.IMAGE_URL));

        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.articleImage);
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        if(cursor != null){
            cursor.moveToPosition(position);
            return cursor.getInt(cursor.getColumnIndex(ArticleColumns._ID));
        }

        return 0L;
    }

    public void swapCursor(Cursor newCursor){
        this.cursor = newCursor;

        notifyDataSetChanged();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Long articleId;

        @BindView(R.id.articleImage)
        ImageView articleImage;

        @BindView(R.id.articleTitle)
        TextView articleTitle;

        @BindView(R.id.articleDate)
        TextView articleDate;

        ArticleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            articleListItemClickListener.onArticleClick(articleId, articleImage);
        }
    }

}
