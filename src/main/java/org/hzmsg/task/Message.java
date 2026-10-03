package org.hzmsg.task;

import org.hzmsg.utils.ChatUtil;

public class Message implements Runnable{

    public Message() {
    }

    @Override
    public void run() {
        ChatUtil.serverChat("&aJoin Tatako anticheat QQ-Group! &f1104880063");
        ChatUtil.serverChat("&aJoin Tatako anticheat Discord! &fhttps://discord.gg/MyzWuY5CMG");
        ChatUtil.serverChat("&aHezhong Offical Website https://hezhongkj.top");
    }
}
