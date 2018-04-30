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
import com.udacity.norbi930523.manutdapp.database.players.PlayerColumns;
import com.udacity.norbi930523.manutdapp.fragment.players.PlayerListFragment;
import com.udacity.norbi930523.manutdapp.util.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayersRecyclerViewAdapter extends RecyclerView.Adapter<PlayersRecyclerViewAdapter.PlayerViewHolder> {

    private Cursor cursor;

    private Context context;

    private PlayerListFragment.PlayerListItemClickListener playerListItemClickListener;

    public PlayersRecyclerViewAdapter(Context context, PlayerListFragment.PlayerListItemClickListener playerListItemClickListener){
        super();
        this.context = context;
        this.playerListItemClickListener = playerListItemClickListener;

        setHasStableIds(true);
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View playerListItem = layoutInflater.inflate(R.layout.player_list_item, parent, false);

        return new PlayerViewHolder(playerListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        cursor.moveToPosition(position);

        holder.playerId = cursor.getLong(cursor.getColumnIndex(PlayerColumns._ID));

        /* Player name in list = squadNumber + lastName */
        int squadNumber = cursor.getInt(cursor.getColumnIndex(PlayerColumns.SQUAD_NUMBER));
        String lastName = cursor.getString(cursor.getColumnIndex(PlayerColumns.LAST_NAME));

        String formattedPlayerName = context.getString(R.string.player_name_pattern, squadNumber, lastName);

        TextUtils.setHtmlText(holder.playerName, formattedPlayerName);

        /* Player image */
        String imageUrl = cursor.getString(cursor.getColumnIndex(PlayerColumns.IMAGE_URL));

        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.playerImage);
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

    class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Long playerId;

        @BindView(R.id.playerImage)
        ImageView playerImage;

        @BindView(R.id.playerName)
        TextView playerName;

        PlayerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            playerListItemClickListener.onPlayerClick(playerId, playerImage);
        }
    }

}
