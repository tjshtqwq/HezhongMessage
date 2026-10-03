package org.hzmsg.task;

import org.hzmsg.HezhongMessage;

public class TpsMonitorTask implements Runnable {
    private long lastCheckTime = System.currentTimeMillis();
    private long tickCount = 0;

    @Override
    public void run() {
        tickCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime >= 1000) {
            HezhongMessage.currentTps = tickCount * 1000.0 / (currentTime - lastCheckTime);
            tickCount = 0;
            lastCheckTime = currentTime;
        }
    }
}
