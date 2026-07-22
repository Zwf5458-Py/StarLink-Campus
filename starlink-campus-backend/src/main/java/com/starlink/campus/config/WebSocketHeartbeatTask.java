package com.starlink.campus.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WebSocketHeartbeatTask {

    // 每 30 秒执行一次检查
    @Scheduled(fixedRate = 30000)
    public void cleanUpZombies() {
        WebSocketServer.checkZombieConnections();
    }
}
