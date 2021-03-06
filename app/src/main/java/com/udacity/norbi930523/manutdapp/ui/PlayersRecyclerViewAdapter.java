package com.udacity.norbi930523.manutdapp.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
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
import com.udacity.norbi930523.manutdapp.util.PlayerPositionUtils;
import com.udacity.norbi930523.manutdapp.util.TextUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayersRecyclerViewAdapter extends RecyclerView.Adapter<PlayersRecyclerViewAdapter.PlayerViewHolder> {

    private Map<Integer, Integer> categoryToListPosition;

    private Cursor cursor;

    private Context context;

    private int selectedItemIndex = -1;

    private PlayerListFragment.PlayerListItemClickListener playerListItemClickListener;

    public PlayersRecyclerViewAdapter(Context context, PlayerListFragment.PlayerListItemClickListener playerListItemClickListener){
        super();
        this.context = context;
        this.playerListItemClickListener = playerListItemClickListener;

        setHasStableIds(true);

        categoryToListPosition = new HashMap<>();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        /* Normal player item layout without title */
        int layout = R.layout.player_list_item;

        if(viewType == 1){
            /* Player item layout with a title and without a large top margin */
            layout = R.layout.top_titled_player_list_item;
        } else if(viewType > 1){
            /* Player item layout with a title and with a large top margin to separate the categories */
            layout = R.layout.titled_player_list_item;
        }

        View playerListItem = layoutInflater.inflate(layout, parent, false);

        return new PlayerViewHolder(playerListItem, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        cursor.moveToPosition(position);

        holder.itemView.setSelected(position == selectedItemIndex);

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
                .placeholder(R.drawable.placeholder_circle)
                .error(R.drawable.placeholder_circle)
                .transform(CircleImageTransformation.getInstance())
                .into(holder.playerImage);

        /* http://mikescamell.com/shared-element-transitions-part-4-recyclerview/ */
        String transitionName = context.getString(R.string.player_image_transition, holder.playerId);
        ViewCompat.setTransitionName(holder.playerImage, transitionName);
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

    @Override
    public int getItemViewType(int position) {
        if(cursor == null){
            return 0;
        }

        cursor.moveToPosition(position);

        /* The first item in each player position category (Goalkeepers, Defenders, Midfielders, Forwards)
         * should use the titled layout */
        int positionCategory = cursor.getInt(cursor.getColumnIndex(PlayerColumns.POSITION));

        if(!categoryToListPosition.containsKey(positionCategory)){
            /* This is the first item in the list with this positionCategory:
             * assign this list position to this positionCategory */
            categoryToListPosition.put(positionCategory, position);

            return positionCategory;
        } else {
            int positionCategoryListPosition = categoryToListPosition.get(positionCategory);

            /* Later, we check if the position assigned to this category is the same as our position
             * (i.e.: Am I the first list item in this positionCategory?):
             * if so, return the viewType corresponding to the category, otherwise return the default */
            return positionCategoryListPosition == position ? positionCategory : 0;
        }

    }

    public void swapCursor(Cursor newCursor){
        this.cursor = newCursor;

        notifyDataSetChanged();
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Long playerId;

        @Nullable
        @BindView(R.id.positionCategoryTitle)
        TextView positionCategoryTitle;

        @BindView(R.id.playerImage)
        ImageView playerImage;

        @BindView(R.id.playerName)
        TextView playerName;

        PlayerViewHolder(View itemView, int viewType) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

            if(viewType != 0){
                positionCategoryTitle.setText(PlayerPositionUtils.getCategoryTitleForPosition(viewType));
            }
        }

        @Override
        public void onClick(View v) {
            /* If the selected item changes... */
            if(selectedItemIndex != getAdapterPosition()){
                /* Update the previously selected list item */
                notifyItemChanged(selectedItemIndex);

                /* Update the current list item */
                notifyItemChanged(getAdapterPosition());

                /* Update the selected item index */
                selectedItemIndex = getAdapterPosition();
            }

            playerListItemClickListener.onPlayerClick(playerId, playerImage);
        }

    }

}
