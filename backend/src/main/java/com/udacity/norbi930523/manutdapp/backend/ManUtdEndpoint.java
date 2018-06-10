package com.udacity.norbi930523.manutdapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.udacity.norbi930523.manutdapp.backend.dto.ArticleVO;
import com.udacity.norbi930523.manutdapp.backend.dto.FixtureVO;
import com.udacity.norbi930523.manutdapp.backend.dto.PlayerVO;
import com.udacity.norbi930523.manutdapp.backend.messaging.MessageSender;
import com.udacity.norbi930523.manutdapp.backend.messaging.MessagingTokenManager;
import com.udacity.norbi930523.manutdapp.backend.parser.FixturesJsonParser;
import com.udacity.norbi930523.manutdapp.backend.parser.NewsJsonParser;
import com.udacity.norbi930523.manutdapp.backend.parser.PlayersJsonParser;

import java.util.Date;
import java.util.Map;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "manutd",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.manutdapp.norbi930523.udacity.com",
                ownerName = "backend.manutdapp.norbi930523.udacity.com",
                packagePath = ""
        )
)
public class ManUtdEndpoint {

    @ApiMethod(name = "registerToken", httpMethod = "POST")
    public void registerToken(@Named("token") String token) {
        MessagingTokenManager.getInstance().registerToken(token);
    }

    @ApiMethod(name = "sendNotification", httpMethod = "POST")
    public void sendNotification(Map<String, String> notificationData) {
        System.out.println("Send message");
        MessageSender.getInstance().sendMessage(notificationData);
    }

    @ApiMethod(name = "news", httpMethod = "GET")
    public ArticleVO[] news(@Named("lastUpdate") Date lastUpdate) {
        return NewsJsonParser.getInstance().getJson(lastUpdate);
    }

    @ApiMethod(name = "players", httpMethod = "GET")
    public PlayerVO[] players() {
        return PlayersJsonParser.getInstance().getJson();
    }

    @ApiMethod(name = "fixtures", httpMethod = "GET")
    public FixtureVO[] fixtures() {
        return FixturesJsonParser.getInstance().getJson();
    }

}
