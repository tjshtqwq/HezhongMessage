package org.hzmsg.utils.type;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class SpeedTest {
    private final Location start, end;
    private final Player player;
    private long startTime;
    private boolean allowedToMove;

    public SpeedTest(final Player player, final Location start, final Location end) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.player.teleport(
                new Location(
                        player.getWorld(),
                        start.getX(),
                        start.getY(),
                        start.getZ(),
                        getOptimalRotation()[0],
                        getOptimalRotation()[1]
                )
        );
        this.startTime = System.currentTimeMillis();
        this.allowedToMove = false;
    }

    public boolean finishedTest() {
        return player.getLocation().getX() >= end.getX();
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public double getSpeedInSeconds() {
        final double distance = end.getX() - start.getX();
        final double seconds = (double) getElapsedTime() / 1000L;
        return distance/seconds;
    }

    public double getSpeedInTicks() {
        final double distance = end.getX() - start.getX();
        final long seconds = getElapsedTime() / 50L;
        return distance/seconds;
    }

    public Pair<Double, Double> getSpeedPercentage() {
        return new Pair<Double, Double>(
                (getSpeedInSeconds() / 6.76) * 100,
                (getSpeedInTicks() / 0.338) * 100
        );
    }

    private float[] getOptimalRotation() {
        final Location origin = this.start.clone();
        final Location target = this.end.clone();

        return new float[]{
                origin.setDirection(target.subtract(origin.toVector()).toVector()).getYaw(),
                0F
        };
    }
    public void allowToMove() {
        this.allowedToMove = true;
        this.startTime = System.currentTimeMillis();
    }
}