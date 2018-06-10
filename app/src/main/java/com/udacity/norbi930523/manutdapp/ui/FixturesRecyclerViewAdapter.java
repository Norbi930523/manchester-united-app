package com.udacity.norbi930523.manutdapp.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.norbi930523.manutdapp.R;
import com.udacity.norbi930523.manutdapp.database.fixtures.FixtureColumns;
import com.udacity.norbi930523.manutdapp.util.DateUtils;
import com.udacity.norbi930523.manutdapp.util.TeamLogoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FixturesRecyclerViewAdapter extends RecyclerView.Adapter<FixturesRecyclerViewAdapter.FixtureViewHolder> {

    private Cursor cursor;

    private Context context;

    public FixturesRecyclerViewAdapter(Context context){
        super();
        this.context = context;

        setHasStableIds(true);
    }

    @NonNull
    @Override
    public FixtureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View fixtureListItem = layoutInflater.inflate(R.layout.fixture_list_item, parent, false);

        return new FixtureViewHolder(fixtureListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FixtureViewHolder holder, int position) {
        cursor.moveToPosition(position);

        long fixtureDateMillis = cursor.getLong(cursor.getColumnIndex(FixtureColumns.DATE));
        String opponent = cursor.getString(cursor.getColumnIndex(FixtureColumns.OPPONENT));
        String venue = cursor.getString(cursor.getColumnIndex(FixtureColumns.VENUE));
        String competition = cursor.getString(cursor.getColumnIndex(FixtureColumns.COMPETITION));
        String result = cursor.getString(cursor.getColumnIndex(FixtureColumns.RESULT));

        holder.fixtureDate.setText(DateUtils.formatDate(fixtureDateMillis));
        holder.fixtureOpponent.setText(String.format("v %s (%s)", opponent, venue));

        if(result == null){
            holder.fixtureCompetition.setText(competition);
        } else {
            holder.fixtureCompetition.setText(String.format("%s, %s", result, competition));
        }

        Picasso.with(context)
                .load(TeamLogoUtils.getTeamLogoResourceId(opponent))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.fixtureOpponentLogo);
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        if(cursor != null){
            cursor.moveToPosition(position);
            return cursor.getInt(cursor.getColumnIndex(FixtureColumns._ID));
        }

        return 0L;
    }

    public void swapCursor(Cursor newCursor){
        this.cursor = newCursor;

        notifyDataSetChanged();
    }

    class FixtureViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fixtureOpponentLogo)
        ImageView fixtureOpponentLogo;

        @BindView(R.id.fixtureDate)
        TextView fixtureDate;

        @BindView(R.id.fixtureOpponent)
        TextView fixtureOpponent;

        @BindView(R.id.fixtureCompetition)
        TextView fixtureCompetition;

        FixtureViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
