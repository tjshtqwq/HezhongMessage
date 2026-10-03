package org.hzmsg.data;

import lombok.Getter;
import lombok.Setter;
import org.hzmsg.Anticheat;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlayerData {

    private boolean enableDamageShow = false;
    private boolean noDamage = true;
    private boolean noFall = true;
    private boolean alert = true;
    private long lastVelocityApply = 0L;
    private List<Anticheat> selectedAnticheats = new ArrayList<>();

    public PlayerData() {
        selectedAnticheats.add(Anticheat.VANILLA);
    }
}
