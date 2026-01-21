package com.jjeanniard.plugins.services;

import com.jjeanniard.plugins.config.MyConfig;

public final class WelcomeService {
    private final String firstJoinMessage;
    private final String rejoinMessage;

    public WelcomeService(MyConfig config) {
        this.firstJoinMessage = config.welcome.firstJoin;
        this.rejoinMessage = config.welcome.rejoin;
    }

    public String getWelcomeMessage(boolean isFirstJoin) {
        return isFirstJoin ? firstJoinMessage : rejoinMessage;
    }
}
