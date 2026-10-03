package org.hzmsg.utils.type;

import org.bukkit.entity.Player;

public class TPARequest {
    public Player sender;
    public Player receiver;
    public boolean accepted;
    public long time;
    public TPARequest(Player sender, Player receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.accepted = false;
        this.time = System.currentTimeMillis();
    }
}
