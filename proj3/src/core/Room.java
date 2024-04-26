package core;

import utils.RandomUtils;

import java.util.Random;

public class Room {
    private int startX;
    private int startY;
    private int width;
    private int height;
    private int CenterX;
    private int CenterY;

    public Room(int startX, int startY, int width, int height, Random random) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.CenterX = RandomUtils.uniform(random, startX + 1, startX + width - 2);
        this.CenterY = RandomUtils.uniform(random, startY + 1, startY + height - 1);
    }

    public int getCenterY() {
        return CenterY;
    }
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public int getCenterX() {
        return CenterX;
    }

    public int getX() {
        return CenterX;
    }

    public int getY() {
        return CenterY;
    }

    public double distance(Room room) {
        return Math.sqrt(Math.pow((room.getX() - this.getX()), 2) + Math.pow((room.getY() - this.getY()), 2));
    }
}

