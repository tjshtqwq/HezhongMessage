package org.hzmsg.task;

import org.hzmsg.HezhongMessage;
import org.hzmsg.utils.ChatUtil;

public class ScaffoldCleanAlertTask implements Runnable {
    @Override
    public void run() {
        if ((HezhongMessage.nextScaffoldClean - System.currentTimeMillis()) <= 300000) {
            if (!HezhongMessage.scaffoldCleanAlertOn5Minutes) {
                HezhongMessage.scaffoldCleanAlertOn5Minutes = true;
                ChatUtil.serverChat("&eWARNING &cScaffold area will be cleared in 5 minutes!");
            }
        }
        if ((HezhongMessage.nextScaffoldClean - System.currentTimeMillis()) <= 60000) {
            if (!HezhongMessage.scaffoldCleanAlertOn1Minutes) {
                HezhongMessage.scaffoldCleanAlertOn1Minutes = true;
                ChatUtil.serverChat("&eWARNING &cScaffold area will be cleared in 1 minute!");
            }
        }
        if ((HezhongMessage.nextScaffoldClean - System.currentTimeMillis()) <= 30000) {
            if (!HezhongMessage.scaffoldCleanAlertOn30Seconds) {
                HezhongMessage.scaffoldCleanAlertOn30Seconds = true;
                ChatUtil.serverChat("&eWARNING &cScaffold area will be cleared in 30 seconds!");
            }
        }
        if ((HezhongMessage.nextScaffoldClean - System.currentTimeMillis()) <= 5000) {
            if (!HezhongMessage.scaffoldCleanAlertOn5Seconds) {
                HezhongMessage.scaffoldCleanAlertOn5Seconds = true;
                ChatUtil.serverChat("&eWARNING &cScaffold area will be cleared in 5 seconds!");
            }
        }
    }
}
