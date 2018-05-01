package com.udacity.norbi930523.manutdapp.util;

import android.content.Context;

import com.udacity.norbi930523.manutdapp.R;

public class PlayerPositionUtils {

    public static final int GOALKEEPER = 1;
    public static final int DEFENDER = 2;
    public static final int MIDFIELDER = 3;
    public static final int FORWARD = 4;

    private PlayerPositionUtils(){}

    public static int valueOf(Context context, String positionStr){
        if(context.getString(R.string.position_goalkeeper).equalsIgnoreCase(positionStr)){
            return GOALKEEPER;
        } else if(context.getString(R.string.position_defender).equalsIgnoreCase(positionStr)){
            return DEFENDER;
        } else if(context.getString(R.string.position_midfielder).equalsIgnoreCase(positionStr)){
            return MIDFIELDER;
        } else if(context.getString(R.string.position_forward).equalsIgnoreCase(positionStr)){
            return FORWARD;
        } else {
            throw new IllegalArgumentException("Unknown position: " + positionStr);
        }
    }

    public static int getCategoryTitleForPosition(int positionId){
        switch (positionId){
            case GOALKEEPER:
                return R.string.category_goalkeepers;
            case DEFENDER:
                return R.string.category_defenders;
            case MIDFIELDER:
                return R.string.category_midfielders;
            case FORWARD:
                return R.string.category_forwards;
        }

        return 0;
    }

    public static String toString(Context context, int positionId) {
        switch (positionId){
            case GOALKEEPER:
                return context.getString(R.string.position_goalkeeper);
            case DEFENDER:
                return context.getString(R.string.position_defender);
            case MIDFIELDER:
                return context.getString(R.string.position_midfielder);
            case FORWARD:
                return context.getString(R.string.position_forward);
        }

        return null;
    }
}
