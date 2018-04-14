package com.udacity.norbi930523.manutdapp.backend.parser;

import com.udacity.norbi930523.manutdapp.backend.dto.ArticleVO;

public class NewsJsonParser extends AbstractJsonParser<ArticleVO[]> {

    private static final NewsJsonParser INSTANCE = new NewsJsonParser();

    private NewsJsonParser(){ }

    public static NewsJsonParser getInstance(){
        return INSTANCE;
    }

    public ArticleVO[] getJson(){
        return getJson("news.json", ArticleVO[].class);
    }

}
