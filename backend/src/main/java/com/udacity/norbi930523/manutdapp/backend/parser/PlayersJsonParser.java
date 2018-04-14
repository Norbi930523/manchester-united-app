package com.udacity.norbi930523.manutdapp.backend.parser;

import com.udacity.norbi930523.manutdapp.backend.dto.PlayerVO;

public class PlayersJsonParser extends AbstractJsonParser<PlayerVO[]> {

    private static final PlayersJsonParser INSTANCE = new PlayersJsonParser();

    private PlayersJsonParser(){ }

    public static PlayersJsonParser getInstance(){
        return INSTANCE;
    }

    public PlayerVO[] getJson(){
        return getJson("players.json", PlayerVO[].class);
    }

}
