package core;

import java.awt.*;
import java.util.*;
import java.util.List;

import tileengine.TETile;
import tileengine.Tileset;

public class World {
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 40;
    private static final int HUD_HEIGHT = 4;
    private static final int TOTAL_HEIGHT = HUD_HEIGHT + DEFAULT_HEIGHT;
    private TETile[][] world;
    private static final int MIN_WIDTH = 8;
    private static final int MIN_HEIGHT = 6;
    private static final int MAX_WIDTH = 14;
    private static final int MAX_HEIGHT = 12;
    private static final double RATIO = 0.40;
    private List<Room> listOfRoom;
    private List<Room> unvisted;
    private Room firstRoom = null;
    private Avatar avatar;




    public World(long seed) {
        world = new TETile[DEFAULT_WIDTH][TOTAL_HEIGHT];
        listOfRoom = new ArrayList<>();
        unvisted = new ArrayList<>();
        fillWithNothing(world);
        Random random = new Random(seed);
        generateRooms(random);
        generateHallway();
        wallForHallway();
        avatarFirstLocation();
        generateDoor(random);
        generateCoin();
    }

    public void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public void generateRooms(Random random) {
        double area = DEFAULT_HEIGHT * DEFAULT_WIDTH;
        double min = area * RATIO;
        double areaNow = 0.0;
        int overlapCount = 0;

        while (areaNow < min) {
            int roomWidth = random.nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
            int roomHeight = random.nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;
            int startX = random.nextInt(DEFAULT_WIDTH - roomWidth - 1);
            int startY = random.nextInt(DEFAULT_HEIGHT - roomHeight - 1);

            if (startX == 0) {
                startX += 1;
            }
            if (startY == 0) {
                startY += 1;
            }

            boolean overlap = false;

            for (int x = startX - 1; x <= startX + roomWidth; x++) {
                for (int y = startY - 1; y <= startY + roomHeight; y++) {
                    if (!world[x][y].equals(Tileset.NOTHING)) {
                        overlap = true;
                    }
                }
            }

            if (overlapCount > 100) {
                break;
            } else if (overlap) {
                overlapCount += 1;
                continue;
            } else {
                Room room = new Room(startX, startY, roomWidth, roomHeight, random);
                listOfRoom.add(room);
                unvisted.add(room);
                for (int x = startX; x < startX + roomWidth; x++) {
                    for (int y = startY; y < startY + roomHeight; y++) {
                        if (x == startX || x == startX + roomWidth - 1 || y == startY || y == startY + roomHeight - 1) {
                            world[x][y] = Tileset.WALL;
                        } else if (x == room.getX() && y == room.getY()) {
                            world[x][y] = Tileset.FLOOR;
                        } else {
                            world[x][y] = Tileset.FLOOR;
                        }
                    }
                }
                if (listOfRoom.size() == 1) {
                    firstRoom = room;
                }
                areaNow += roomHeight * roomWidth;
            }

        }
    }

    public void generateHallway() {
        Room room = listOfRoom.get(0);
        while (unvisted.size() != 1) {
            room = goNext(room);
        }
        connect(room, unvisted.get(0));
    }

    public void wallForHallway() {
        for (int x = 1; x < DEFAULT_WIDTH - 1; x++) {
            for (int y = 1; y < DEFAULT_HEIGHT - 1; y++) {
                if (world[x][y].equals(Tileset.FLOOR)) {
                    if (world[x + 1][y].equals(Tileset.NOTHING)) {
                        world[x + 1][y] = Tileset.WALL;
                    }
                    if (world[x - 1][y].equals(Tileset.NOTHING)) {
                        world[x - 1][y] = Tileset.WALL;
                    }
                    if (world[x][y + 1].equals(Tileset.NOTHING)) {
                        world[x][y + 1] = Tileset.WALL;
                    }
                    if (world[x][y - 1].equals(Tileset.NOTHING)) {
                        world[x][y - 1] = Tileset.WALL;
                    }
                }
            }
        }
    }
    public void generateDoor(Random random) {
        List<Room> otherRoom = new ArrayList<>(listOfRoom);
        Room firstRoom1 = listOfRoom.get(otherRoom.size() / 2);
        //Room secondRoom = listOfRoom.get(2);
        otherRoom.remove(firstRoom1);
        //otherRoom.remove(secondRoom);
        world[firstRoom1.getCenterX() + 1][firstRoom1.getStartY()] = Tileset.UNLOCKED_DOOR;
        //world[secondRoom.getCenterX()+1][secondRoom.getStartY()] = Tileset.UNLOCKED_DOOR;
    }
    public void generateCoin() {
        for (Room room : listOfRoom) {
            world[room.getCenterX()][room.getCenterY()] = Tileset.LOCKED_DOOR;
        }
    }

    public int numRoom() {
        return listOfRoom.size();
    }


    public Room goNext(Room room) {
        unvisted.remove(room);
        Room targetRoom = closeRoom(room);
        connect(room, targetRoom);
        return targetRoom;
    }

    public Room closeRoom(Room room) {
        List<Room> copy = new ArrayList<>(unvisted);
        Room min = copy.get(0);
        for (int i = 0; i < copy.size(); i++) {
            double dist1 = room.distance(min);
            double dist2 = room.distance(copy.get(i));
            if (dist2 < dist1) {
                min = copy.get(i);
            }
        }
        return min;
    }

    public void connect(Room start, Room end) {
        int startX = start.getX();
        int startY = start.getY();
        int endX = end.getX();
        int endY = end.getY();
        int x = startX;
        int y = startY;
        if (endY > startY) {
            while (endY != y) {
                world[x][y] = Tileset.FLOOR;
                y++;
            }
        } else if (endY < startY) {
            while (endY != y) {
                world[x][y] = Tileset.FLOOR;
                y--;
            }
        }

        if (endX > startX) {
            while (endX != x) {
                world[x][y] = Tileset.FLOOR;
                x++;
            }
        } else if (endX < startX) {
            while (endX != x) {
                world[x][y] = Tileset.FLOOR;
                x--;
            }
        }

    }

    public TETile[][] getWorld() {
        return world;
    }

    public int getWidth() {
        return DEFAULT_WIDTH;
    }

    public int getHeight() {
        return DEFAULT_HEIGHT;
    }

    public int getHudHeight() {
        return HUD_HEIGHT;
    }

    public int getTotalHeight() {
        return TOTAL_HEIGHT;
    }
    // generate first location of the avatar
    public Avatar avatarFirstLocation() {
        avatar = new Avatar(firstRoom.getX(), firstRoom.getY());
        return avatar;
    }
}
