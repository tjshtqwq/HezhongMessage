package org.hzmsg.utils;

public class PlayerPosition {
    private double X;
    private double Y;
    private double Z;
    private double LastX;
    private double LastY;
    private double LastZ;

    public PlayerPosition(double x, double z) {
        this.X = x;
        this.Z = z;
        this.LastX = x;
        this.LastZ = z;
    }

    public void updatePosition(double newX, double newY, double newZ) {
        this.LastX = this.X;
        this.LastY = this.Y;
        this.LastZ = this.Z;
        this.X = newX;
        this.Y = newY;
        this.Z = newZ;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getZ() {
        return Z;
    }

    public double getLastX() {
        return LastX;
    }

    public double getLastY() {
        return LastY;
    }

    public double getLastZ() {
        return LastZ;
    }
}

