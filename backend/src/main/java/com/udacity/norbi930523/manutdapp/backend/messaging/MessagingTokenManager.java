package com.udacity.norbi930523.manutdapp.backend.messaging;

import java.util.HashSet;
import java.util.Set;

public class MessagingTokenManager {

    private Set<String> registeredTokens;

    private static final MessagingTokenManager INSTANCE = new MessagingTokenManager();

    private MessagingTokenManager(){
        registeredTokens = new HashSet<>();
    }

    public static MessagingTokenManager getInstance(){
        return INSTANCE;
    }

    public void registerToken(String token){
        registeredTokens.add(token);
    }

    public Set<String> getRegisteredTokens(){
        return registeredTokens;
    }

}
