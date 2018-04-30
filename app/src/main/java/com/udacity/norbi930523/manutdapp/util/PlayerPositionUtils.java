package com.udacity.norbi930523.manutdapp.util;

import com.udacity.norbi930523.manutdapp.R;

public class PlayerPositionUtils {

    public static final int GOALKEEPER = 1;
    public static final int DEFENDER = 2;
    public static final int MIDFIELDER = 3;
    public static final int FORWARD = 4;

    private PlayerPositionUtils(){}

    public static int valueOf(String positionStr){
        if("Goalkeeper".equalsIgnoreCase(positionStr)){
            return GOALKEEPER;
        } else if("Defender".equalsIgnoreCase(positionStr)){
            return DEFENDER;
        } else if("Midfielder".equalsIgnoreCase(positionStr)){
            return MIDFIELDER;
        } else if("Forward".equalsIgnoreCase(positionStr)){
            return FORWARD;
        } else {
            throw new IllegalArgumentException("Unknown position: " + positionStr);
        }
    }

    public static int getCategoryTitleForPosition(int position){
        switch (position){
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

}
