package com.udacity.norbi930523.manutdapp.backend.parser;

import com.udacity.norbi930523.manutdapp.backend.dto.ArticleVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsJsonParser extends AbstractJsonParser<ArticleVO[]> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final NewsJsonParser INSTANCE = new NewsJsonParser();

    private NewsJsonParser(){ }

    public static NewsJsonParser getInstance(){
        return INSTANCE;
    }

    public ArticleVO[] getJson(){
        return getJson("news.json", ArticleVO[].class);
    }

    public ArticleVO[] getJson(Date lastUpdate){
        ArticleVO[] articles = getJson();

        List<ArticleVO> articlesSinceLastUpdate = new ArrayList<>();
        for(ArticleVO article : articles){
            Date articleDate = parseArticleDate(article.getDate());

            if(lastUpdate == null || !articleDate.before(lastUpdate)){
                articlesSinceLastUpdate.add(article);
            }
        }

        return articlesSinceLastUpdate.toArray(new ArticleVO[0]);
    }

    private Date parseArticleDate(String articleDateStr){
        try {
            return DATE_FORMAT.parse(articleDateStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

}
