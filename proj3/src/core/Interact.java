package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.awt.*;

import static org.apache.commons.lang3.StringUtils.isNumeric;

// logic of the game
public class Interact {

    private TERenderer ter;
    private World world;
    private TETile[][] worldTETile;
    private Avatar avatar;
    private String input = "";
    private boolean inDisplayMenu = true;
    private boolean inGamePage = true;
    private static final String SAVE_FILE = "save.txt";
    private boolean finishedStringInput = false;
    private int coin;
    private TETile[][] lightOffWorld;
    private boolean light = false;
    private TETile avatarSkin = Tileset.AVATAR;


    public Interact() {
        ter = new TERenderer();
    }

    // Source: inspired by the code that ChatGPT generated for StdDraw part
    public void displayMenu() {
        // Set up canvas
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 600);
        StdDraw.enableDoubleBuffering();

        // Set font properties
        Font font = new Font("Monospaced", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);

        // Clear canvas
        StdDraw.clear(StdDraw.BLACK);

        // Display game name with bigger font
        StdDraw.text(400, 450, "A Maze Kween");

        // Display menu options
        StdDraw.setFont(new Font("Monospaced", Font.PLAIN, 20));
        StdDraw.text(400, 350, "Change Avatar (B = Berry, M = Meat, O = SWORD, no type = DEFAULT)");
        StdDraw.text(400, 300, "New Game (N)");
        StdDraw.text(400, 250, "Load Game (L)");
        StdDraw.text(400, 200, "Quit (Q)");

        // Show the updated canvas
        StdDraw.show();
    }

    // Source: inspired by the code that ChatGPT generated for StdDraw part
    public void seedPage() {
        // Set up canvas
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 600);
        StdDraw.enableDoubleBuffering();

        // Set font properties
        Font font = new Font("Monospaced", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.clear(StdDraw.BLACK);

        boolean finishedInput = false;

        String inputSeed = "";
        while (!finishedInput) {

            // Clear canvas
            StdDraw.clear(StdDraw.BLACK);

            // Display prompt
            StdDraw.text(400, 450, "Type the seed down here:");

            // Display rectangular blank
            StdDraw.rectangle(400, 400, 200, 30);

            // Display current input
            StdDraw.text(400, 400, inputSeed);

            StdDraw.text(400, 350, "Type S once finishing");

            // Display updated canvas
            StdDraw.show();

            Character avatarInitial = Character.MIN_VALUE;
            boolean customAvatar = false;
            if (input.length() == 1 && (input.charAt(0) == 'B' || input.charAt(0) == 'b'
                    || input.charAt(0) == 'M' || input.charAt(0) == 'm'
                    || input.charAt(0) == 'O' || input.charAt(0) == 'o')) {
                avatarInitial = input.charAt(0);
                customAvatar = true;
            }

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (Character.isDigit(key)) {
                    inputSeed += key;
                } else if ((key == 'S' || key == 's')  && !inputSeed.isEmpty()) { // Enter key
                    finishedInput = true;
                    if (customAvatar) {
                        input = avatarInitial + inputSeed;
                    } else {
                        input = inputSeed;
                    }
                    gamePage(true);
                    input += key;
                }
            }
        }
    }
    public void gamePage(Boolean isKeyboardInput) {
        if (isNumeric(input)) {
            world = new World(Long.parseLong(input));
        } else {
            world = new World(Long.parseLong(input.substring(1)));
        }
        if (isKeyboardInput) {
            ter.initialize(world.getWidth(), world.getTotalHeight());
        }
        worldTETile = world.getWorld();
        avatar = new Avatar(world.avatarFirstLocation().getxA(), world.avatarFirstLocation().getyA());
        worldTETile[avatar.getxA()][avatar.getyA()] = avatarSkin;
        lightOffWorld = new TETile[80][40];
        coin = world.numRoom();
        updateNoLightWorld();
        inDisplayMenu = false;
    }
    public void updateNoLightWorld() {
        world.fillWithNothing(lightOffWorld);

        int x = avatar.getxA();
        int y = avatar.getyA();

        for (int i = -3; i < 3; i++) {
            for (int j = -3; j < 3; j++) {
                if (x + i >= 0 && x + i < 80 && y + j >= 0 && y + j < 40) {
                    lightOffWorld[x + i][y + j] = worldTETile[x + i][y + j];
                }
            }
        }
    }
    public void handleKeyBoardUserInput() {
        while (inDisplayMenu) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'B' || key == 'b'
                        || key == 'M' || key == 'm'
                        || key == 'O' || key == 'o') {
                    input += key;
                    changeAvatarAppearance(key);
                } else if (key == 'N' || key == 'n') {
                    if (input.length() > 0 && (input.charAt(0) == 'B' || input.charAt(0) == 'b'
                            || input.charAt(0) == 'M' || input.charAt(0) == 'm'
                            || input.charAt(0) == 'O' || input.charAt(0) == 'o')) {
                        save(String.valueOf(input.charAt(0))); // start a new save
                    } else {
                        save(""); // start a new save
                    }
                    seedPage();
                }  else if (key == 'L' || key == 'l') {
                    load(SAVE_FILE);
                    ter.initialize(world.getWidth(), world.getTotalHeight());
                    worldTETile = world.getWorld();
                    inDisplayMenu = false;
                } else if ((key == 'Q' || key == 'q')) {
                    if (input.length() > 0 && input.charAt(input.length() - 1) == ':') {
                        inGamePage = false;
                        input += key;
                        save(input);
                        quit();
                        break;
                    } else {
                        quit();     // In display menu, if user types "Q" or "q," just quit the game
                    }
                } else if (key == ':') {
                    input += key;
                }
            }
        }
    }
    public void finishGame() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 22, "Congrats, you have escaped.");
        StdDraw.text(40, 20, "Press any key to quit.");
        StdDraw.show();
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
    }
    public void runGame() {
        updateNoLightWorld();
        while (inGamePage) {
            updateBoard();
            if (light) {
                renderBoard();
            } else {
                renderNoLightBoard();
            }
            StdDraw.clear(StdDraw.BLACK);
        }
    }
    public void updateBoard() {
        if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            if ((key == 'A' || key == 'a')
                    || (key == 'S' || key == 's')
                    || (key == 'D' || key == 'd')
                    || (key == 'W' || key == 'w')) {
                avatarMovement(key);
                updateNoLightWorld();
                input += key;
            } else if ((key == 'q' || key == 'Q') && input.charAt(input.length() - 1) == ':') {
                input += key;
                save(input);
                quit();
            } else if (key == 'r' || key == 'R') {
                light = !light;
                updateNoLightWorld();
            } else if (key == ':') {
                input += key;
            }
        }
    }
    public void renderHud() {
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.textLeft(0, 44 - 1, "Welcome to the World");
        StdDraw.textLeft(0, 42 - 1, "Press : and then q to save the game");

        StdDraw.textLeft(30, 44 - 1, "Find the unlocked door and escape this world");
        StdDraw.textLeft(30, 42 - 1, "The coins are super helpful, you get " + coin + " coins left");
        StdDraw.textLeft(30, 40 - 1, "Press R to turn on/off the light");


        String at = "";
        int x = ((int) Math.floor(StdDraw.mouseX()));
        int y = ((int) Math.floor(StdDraw.mouseY()));

        if (x >= 0 && x < 80 && y >= 0 && y < 44) {
            TETile tile = worldTETile[x][y];
            if (tile.equals(Tileset.NOTHING)) {
                at = "nothing";
            } else if (tile.equals(Tileset.WALL)) {
                at = "wall";
            } else if (tile.equals(avatarSkin)) {
                if (avatarSkin.equals(Tileset.AVATAR)) {
                    at = "you";
                } else if (avatarSkin.equals(Tileset.AVATAR_BERRY)) {
                    at = "you berry";
                } else if (avatarSkin.equals(Tileset.AVATAR_MEAT)) {
                    at = "you meat";
                } else if (avatarSkin.equals(Tileset.AVATAR_SWORD)) {
                    at = "you sword";
                }
            } else if (tile.equals(Tileset.FLOOR)) {
                at = "floor";
            } else if (tile.equals(Tileset.LOCKED_DOOR)) {
                at = "coin";
            } else if (tile.equals(Tileset.UNLOCKED_DOOR)) {
                at = "unlocked door";
            }
            StdDraw.textRight(80, 44 - 1, "You are at: " + at);
        }

    }
    public void renderBoard() {
        ter.drawTiles(worldTETile);
        renderHud();
        StdDraw.show();
    }
    public void renderNoLightBoard() {
        ter.drawTiles(lightOffWorld);
        renderHud();
        StdDraw.show();
    }

    public World handlerStringUserInput(String command, Boolean isLoadFromKeyboard) {
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == 'B' || command.charAt(i) == 'b'
                    || command.charAt(i) == 'M' || command.charAt(i) == 'm'
                    || command.charAt(i) == 'O' || command.charAt(i) == 'o') {
                input += command.charAt(i);
                changeAvatarAppearance(command.charAt(i));
            } else if (command.charAt(i) == 'N' || command.charAt(i) == 'n') {
                if (command.length() > 1 && (command.charAt(0) == 'B' || command.charAt(0) == 'b'
                        || command.charAt(0) == 'M' || command.charAt(0) == 'm'
                        || command.charAt(0) == 'O' || command.charAt(0) == 'o')) {
                    save(String.valueOf(command.charAt(0))); // start a new save
                } else {
                    save(""); // start a new save
                }
            } else if (Character.isDigit(command.charAt(i)) && !finishedStringInput) {
                input += command.charAt(i);
            } else if ((command.charAt(i) == 'S' || command.charAt(i) == 's')
                    && Character.isDigit(command.charAt(i - 1))) {
                finishedStringInput = true;
                gamePage(isLoadFromKeyboard);
                input += command.charAt(i);
            } else if (command.charAt(i) == 'L' || command.charAt(i) == 'l') {
                load(SAVE_FILE);
                worldTETile = world.getWorld();
                inDisplayMenu = false;
                input += command.charAt(i);
            } else if ((command.charAt(i) == 'Q' || command.charAt(i) == 'q') && command.charAt(i - 1) == ':') {
                input = input.substring(0, input.length() - 1);
                save(input);
                break;
            } else {
                avatarMovement(command.charAt(i));
                input += command.charAt(i);
            }
        }
        return world;
    }

    public void avatarMovement(char movement) {
        if ((movement == 'A' || movement == 'a') && worldTETile[avatar.getxA() - 1][avatar.getyA()] != Tileset.WALL) {
            Avatar newLocation = avatar.left();
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.UNLOCKED_DOOR)) {
                inGamePage = false;
            }
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.LOCKED_DOOR)) {
                coin--;
            }
            worldTETile[newLocation.getxA()][newLocation.getyA()] = avatarSkin;
            worldTETile[avatar.getxA()][avatar.getyA()] = Tileset.FLOOR;
            avatar = newLocation;
        } else if ((movement == 'S' || movement == 's')
                && worldTETile[avatar.getxA()][avatar.getyA() - 1] != Tileset.WALL) {
            Avatar newLocation = avatar.down();
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.UNLOCKED_DOOR)) {
                inGamePage = false;
            }
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.LOCKED_DOOR)) {
                coin--;
            }
            worldTETile[newLocation.getxA()][newLocation.getyA()] = avatarSkin;
            worldTETile[avatar.getxA()][avatar.getyA()] = Tileset.FLOOR;
            avatar = newLocation;
        } else if ((movement == 'D' || movement == 'd')
                && worldTETile[avatar.getxA() + 1][avatar.getyA()] != Tileset.WALL) {
            Avatar newLocation = avatar.right();
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.UNLOCKED_DOOR)) {
                inGamePage = false;
            }
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.LOCKED_DOOR)) {
                coin--;
            }
            worldTETile[newLocation.getxA()][newLocation.getyA()] = avatarSkin;
            worldTETile[avatar.getxA()][avatar.getyA()] = Tileset.FLOOR;
            avatar = newLocation;
        } else if ((movement == 'W' || movement == 'w')
                && worldTETile[avatar.getxA()][avatar.getyA() + 1] != Tileset.WALL) {
            Avatar newLocation = avatar.up();
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.UNLOCKED_DOOR)) {
                inGamePage = false;
            }
            if (worldTETile[newLocation.getxA()][newLocation.getyA()].equals(Tileset.LOCKED_DOOR)) {
                coin--;
            }
            worldTETile[newLocation.getxA()][newLocation.getyA()] = avatarSkin;
            worldTETile[avatar.getxA()][avatar.getyA()] = Tileset.FLOOR;
            avatar = newLocation;
        }
    }

    public void changeAvatarAppearance(char skin) {
        if (skin == 'b' || skin == 'B') {
            avatarSkin = Tileset.AVATAR_BERRY;
        } else if (skin == 'm' || skin == 'M') {
            avatarSkin = Tileset.AVATAR_MEAT;
        } else if (skin == 'o' || skin == 'O') {
            avatarSkin = Tileset.AVATAR_SWORD;
        }
    }

    public void load(String filename) {
        String content = "";
        if (FileUtils.fileExists(filename)) {
            content = FileUtils.readFile(filename);
        } else {
            quit();
        }

        world = handlerStringUserInput(content, false);
    }

    public void save(String command) {      // String command: Example: N345SDSWAAASSDW
        FileUtils.writeFile(SAVE_FILE, command);
    }

    public void quit() {
        System.exit(0);
    }
}
