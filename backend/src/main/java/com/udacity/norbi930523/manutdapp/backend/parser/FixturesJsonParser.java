package com.udacity.norbi930523.manutdapp.backend.parser;

import com.udacity.norbi930523.manutdapp.backend.dto.FixtureVO;

public class FixturesJsonParser extends AbstractJsonParser<FixtureVO[]> {

    private static final FixturesJsonParser INSTANCE = new FixturesJsonParser();

    private FixturesJsonParser(){ }

    public static FixturesJsonParser getInstance(){
        return INSTANCE;
    }

    public FixtureVO[] getJson(){
        return getJson("fixtures.json", FixtureVO[].class);
    }

}
