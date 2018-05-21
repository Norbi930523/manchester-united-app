package com.udacity.norbi930523.manutdapp.util;

import com.udacity.norbi930523.manutdapp.R;

public class TeamLogoUtils {

    private TeamLogoUtils(){}

    public static int getTeamLogoResourceId(String teamName){
        switch (teamName){
            case "Arsenal":
                return R.drawable.arsenal;
            case "Bournemouth":
                return R.drawable.bournemouth;
            case "Brighton & Hove Albion":
                return R.drawable.brighton;
            case "Burnley":
                return R.drawable.burnley;
            case "Chelsea":
                return R.drawable.chelsea;
            case "Crystal Palace":
                return R.drawable.crystal_palace;
            case "Everton":
                return R.drawable.everton;
            case "Huddersfield Town":
                return R.drawable.huddersfield;
            case "Leicester City":
                return R.drawable.leicester;
            case "Liverpool":
                return R.drawable.liverpool;
            case "Manchester City":
                return R.drawable.man_city;
            case "Newcastle United":
                return R.drawable.newcastle;
            case "Southampton":
                return R.drawable.southampton;
            case "Stoke City":
                return R.drawable.stoke;
            case "Swansea City":
                return R.drawable.swansea;
            case "Tottenham Hotspur":
                return R.drawable.tottenham;
            case "Watford":
                return R.drawable.watford;
            case "West Brom Albion":
                return R.drawable.west_brom;
            case "West Ham United":
                return R.drawable.west_ham;
            case "FC Basel":
                return R.drawable.basel;
            case "Benfica":
                return R.drawable.benfica;
            case "Bristol City":
                return R.drawable.bristol_city;
            case "Burton Albion":
                return R.drawable.burton_albion;
            case "CSKA Moscow":
                return R.drawable.cska_moscow;
            case "Derby County":
                return R.drawable.derby_county;
            case "Real Madrid":
                return R.drawable.real_madrid;
            case "Sevilla":
                return R.drawable.sevilla;
            case "Yeovil Town":
                return R.drawable.yeovil_town;
            default:
                return R.drawable.placeholder;
        }
    }

}
