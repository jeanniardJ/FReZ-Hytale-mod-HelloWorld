package com.jjeanniard.mods;

public final class WelcomeService {
    private final String firstJoinMessage;
    private final String rejoinMessage;

    public WelcomeService(MyConfig config) {
        
        this.firstJoinMessage = config.getFirstJoinMessage();
        this.rejoinMessage = config.getRejoinMessage();
    }

    public String getWelcomeMessage(boolean isFirstJoin) {
        return isFirstJoin ? firstJoinMessage : rejoinMessage;
    }
}
