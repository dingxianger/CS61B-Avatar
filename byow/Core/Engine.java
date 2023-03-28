package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import java.awt.*;
import java.util.ArrayList;
import java.io.File;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import static java.lang.Character.isDigit;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    public int[] position = new int[2];  // [x, y]
    public TETile[][] tileSet;
    public String seed;
    public String name = "Yourself";
    public boolean startSeed;
    public boolean endSeed = false;


    public Engine(){
        tileSet = new TETile[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tileSet[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        drawFrame();

        seed = "";

        while(true) {

            boolean ifStart = false;
            startSeed =false;

            // n pressed
            if (StdDraw.hasNextKeyTyped()) {
                System.out.print("1");
                if (ifStart == false && StdDraw.isKeyPressed(78)) {
                    StdDraw.clear(Color.BLACK);
                    name = enterName();
                    System.out.println("name finished");
                    StdDraw.clear(Color.BLACK);
                    ifStart = true;
                }
                if (startSeed) {
                    seed = enteringSeed(6, startSeed);
                    System.out.print("finished");
                }
                else if (StdDraw.isKeyPressed(76)) {
                    load();
                    break;
                }
                else if (StdDraw.isKeyPressed(83)) {
                    tileSet = interactWithInputString(seed);
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(tileSet);
                    startMoving("");
                }
                else if (StdDraw.isKeyPressed(82)) {
                    replay();
                    break;
                }
                else if (StdDraw.isKeyPressed(81)) {
                    System.exit(0);
                } else {
                    StdDraw.nextKeyTyped();
                }
            } else if (endSeed) {
                tileSet = interactWithInputString(seed);
                ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                ter.renderFrame(tileSet);
                startMoving("");
                endSeed = false;
            }
        }
    }

    public void hud() {
        int mouseXCord = (int) (StdDraw.mouseX());
        int mouseYCord = (int) (StdDraw.mouseY());
        DateTimeFormatter style = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
        LocalDateTime realDateTime = LocalDateTime.now();
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH - 3, HEIGHT - 0.5, 8, 1);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textRight(WIDTH - 1, HEIGHT - 1, style.format(realDateTime));
        StdDraw.show();
        if (StdDraw.mouseX() < WIDTH - 1 && StdDraw.mouseY() < HEIGHT - 1) {
            if (tileSet[mouseXCord][mouseYCord] == Tileset.WALL) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(2, HEIGHT - 0.5, 10, 1);
                StdDraw.setPenColor(Color.WHITE);
                Font fontSmall = new Font("Monaco", Font.BOLD, 14);
                StdDraw.setFont(fontSmall);
                StdDraw.textLeft(0.5, HEIGHT - 1, "wall");
                StdDraw.show();
            }
            if (tileSet[mouseXCord][mouseYCord] == Tileset.FLOOR) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(2, HEIGHT - 0.5, 10, 1);
                StdDraw.setPenColor(Color.WHITE);
                Font fontSmall = new Font("Monaco", Font.BOLD, 14);
                StdDraw.setFont(fontSmall);
                StdDraw.textLeft(0.5, HEIGHT - 1, "floor");
                StdDraw.show();
            }
            if (tileSet[mouseXCord][mouseYCord] == Tileset.AVATAR) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(2, HEIGHT - 0.5, 10, 1);
                StdDraw.setPenColor(Color.WHITE);
                Font fontSmall = new Font("Monaco", Font.BOLD, 14);
                StdDraw.setFont(fontSmall);
                StdDraw.textLeft(0.5, HEIGHT - 1, name);
                StdDraw.show();
            }
            if (tileSet[mouseXCord][mouseYCord] == Tileset.NOTHING) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(2, HEIGHT - 0.5, 10, 1);
                StdDraw.setPenColor(Color.WHITE);
                Font fontSmall = new Font("Monaco", Font.BOLD, 14);
                StdDraw.setFont(fontSmall);
                StdDraw.textLeft(0.5, HEIGHT - 1, "boundary");
                StdDraw.show();
            }
        }
    }

    public void load() {
        File tmpDir = new File("savefile.txt");
        boolean exists = tmpDir.exists();
        if (exists) {
            In in = new In("savefile.txt");
            String firstLine = in.readLine();
            if (firstLine == null) {
                drawRandom("no game saved, please start a new game");
                if (StdDraw.hasNextKeyTyped()) {
                    while (true) {
                        Character ignoredKey = StdDraw.nextKeyTyped();
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        Font fontBig = new Font("Monaco", Font.BOLD, 30);
                        StdDraw.setFont(fontBig);
                        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                        StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                        StdDraw.show();
                        break;
                    }
                }
                interactWithKeyboard();
            } else {
                Character last = firstLine.charAt(firstLine.length() - 1);
                if (!isDigit(last)) {
                    drawRandom("no game saved, please start a new game");
                    if (StdDraw.hasNextKeyTyped()) {
                        while (true) {
                            Character ignoredKey = StdDraw.nextKeyTyped();
                            StdDraw.clear(Color.BLACK);
                            StdDraw.setPenColor(Color.WHITE);
                            Font fontBig = new Font("Monaco", Font.BOLD, 30);
                            StdDraw.setFont(fontBig);
                            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                            StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                            StdDraw.show();
                            break;
                        }
                    }
                    interactWithKeyboard();
                } else {
                    firstLine = firstLine.substring(0, firstLine.length() - 1);
                    seed = in.readLine();
                    name = in.readLine();
                    if (seed == null || name == null) {
                        drawRandom("no game saved, please start a new game");
                        if (StdDraw.hasNextKeyTyped()) {
                            while (true) {
                                Character ignoredKey = StdDraw.nextKeyTyped();
                                StdDraw.clear(Color.BLACK);
                                StdDraw.setPenColor(Color.WHITE);
                                Font fontBig = new Font("Monaco", Font.BOLD, 30);
                                StdDraw.setFont(fontBig);
                                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                                StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                                StdDraw.show();
                                break;
                            }
                        }
                        interactWithKeyboard();
                    }
                    int currx = in.readInt();
                    int curry = in.readInt();
                    tileSet = interactWithInputString(seed);
                    tileSet[position[0]][position[1]] = Tileset.FLOOR;
                    position[0] = currx;
                    position[1] = curry;
                    tileSet[position[0]][position[1]] = Tileset.AVATAR;
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(tileSet);
                    startMoving(firstLine);
                }
            }
        }
        else {
            drawRandom("no game saved, please start a new game");
            if (StdDraw.hasNextKeyTyped()) {
                while (true) {
                    Character ignoredKey = StdDraw.nextKeyTyped();
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    Font fontBig = new Font("Monaco", Font.BOLD, 30);
                    StdDraw.setFont(fontBig);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                    StdDraw.show();
                    break;
                }
            }
            interactWithKeyboard();
        }
    }

    public void replay() {
        File tmpDir = new File("savefile.txt");
        boolean exists = tmpDir.exists();
        if (exists) {
            In in = new In("savefile.txt");
            String firstLine = in.readLine();
            if (firstLine == null) {
                drawRandom("no game saved, please start a new game");
                if (StdDraw.hasNextKeyTyped()) {
                    while (true) {
                        Character ignoredKey = StdDraw.nextKeyTyped();
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        Font fontBig = new Font("Monaco", Font.BOLD, 30);
                        StdDraw.setFont(fontBig);
                        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                        StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                        StdDraw.show();
                        break;
                    }
                }
                interactWithKeyboard();
            } else {
                Character last = firstLine.charAt(firstLine.length() - 1);
                if (!isDigit(last)) {
                    drawRandom("no game saved, please start a new game");
                    if (StdDraw.hasNextKeyTyped()) {
                        while (true) {
                            Character ignoredKey = StdDraw.nextKeyTyped();
                            StdDraw.clear(Color.BLACK);
                            StdDraw.setPenColor(Color.WHITE);
                            Font fontBig = new Font("Monaco", Font.BOLD, 30);
                            StdDraw.setFont(fontBig);
                            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                            StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                            StdDraw.show();
                            break;
                        }
                    }
                    interactWithKeyboard();
                } else {
                    firstLine.substring(0, firstLine.length() - 1);
                    seed = in.readLine();
                    name = in.readLine();
                    if (seed == null || name == null) {
                        drawRandom("no game saved, please start a new game");
                        if (StdDraw.hasNextKeyTyped()) {
                            while (true) {
                                Character ignoredKey = StdDraw.nextKeyTyped();
                                StdDraw.clear(Color.BLACK);
                                StdDraw.setPenColor(Color.WHITE);
                                Font fontBig = new Font("Monaco", Font.BOLD, 30);
                                StdDraw.setFont(fontBig);
                                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                                StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                                StdDraw.show();
                                break;
                            }
                        }
                        interactWithKeyboard();
                    }
                    tileSet = interactWithInputString(seed);
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(tileSet);
                    if (firstLine.length() > 1) {
                        for (int i = 0; i < firstLine.length() - 1; i++) {
                            if (Character.toString(firstLine.charAt(i)).equals("w")) {
                                up();
                                StdDraw.pause(500);
                            } else if (Character.toString(firstLine.charAt(i)).equals("a")) {
                                left();
                                StdDraw.pause(500);
                            } else if (Character.toString(firstLine.charAt(i)).equals("s")) {
                                down();
                                StdDraw.pause(500);
                            } else if (Character.toString(firstLine.charAt(i)).equals("d")) {
                                right();
                                StdDraw.pause(500);
                            }
                        }
                    }
                    startMoving(firstLine);
                }
            }
        }
        else {
            drawRandom("no game saved, please start a new game");
            if (StdDraw.hasNextKeyTyped()) {
                while (true) {
                    Character ignoredKey = StdDraw.nextKeyTyped();
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    Font fontBig = new Font("Monaco", Font.BOLD, 30);
                    StdDraw.setFont(fontBig);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                    StdDraw.show();
                    break;
                }
            }
            interactWithKeyboard();
        }
    }

    public void startMoving(String previousStep) {
        Out out = new Out("savefile.txt");
        boolean isColonPressed = false;
        out.print(previousStep);
        while (true) {
            hud();
            if (StdDraw.hasNextKeyTyped()) {
                hud();
                String key = Character.toString(StdDraw.nextKeyTyped());
                if (isColonPressed && (key.equals("Q") || key.equals("q"))) {
                    out.println("0");
                    out.println(seed);
                    out.println(name);
                    out.println(position[0]);
                    out.println(position[1]);
                    System.exit(0);
                }
                if (key.equals("w") || key.equals("W")) {
                    up();
                    out.print("w");
                } else if (key.equals("a") || key.equals("A")) {
                    left();
                    out.print("a");
                } else if (key.equals("s") || key.equals("S")) {
                    down();
                    out.print("s");
                } else if (key.equals("d") || key.equals("D")) {
                    right();
                    out.print("d");
                } else if (key.equals(":")) {
                    System.out.print(": pressed");
                    isColonPressed = true;
                } else if (key.equals("Q") || key.equals("q")) {
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.


        /*
        int seed = 0;
        if (input.matches("(N|n)[0-9]+(S|s)")) {
            seed = Integer.parseInt(input.substring(1, input.length() - 1));
        }
         */
        int seed = Integer.parseInt(input);
        Random RANDOM = new Random(seed);

        ArrayList<int[]> rooms = randomRoom(RANDOM);
        randomHallway(rooms);
        walls(WIDTH, HEIGHT);

        return tileSet;
    }


    public void drawFrame() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "New World (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, "Load (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Replay Game (R)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Quit (Q)");
        StdDraw.show();
    }

    public void drawRandom(String line) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, line);
        StdDraw.show();
        StdDraw.pause(2000);
    }

    public void enterSeed(String seed) {
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Seed");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, seed);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Press S to Enter");
        StdDraw.show();
    }

    public String enteringSeed(int n, boolean ifStart) {
        String fullKeys = "";
        int count = 0;
        while (count < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            if (StdDraw.hasNextKeyTyped() && ifStart) {
                Character key = StdDraw.nextKeyTyped();
                if (isDigit(key)) {
                    String tempKey = Character.toString(key);
                    fullKeys += tempKey;
                    count++;
                } else if (key.toString().equals("s") || key.toString().equals("S")) {
                    endSeed = true;
                    break;
                }
            }
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            enterSeed(fullKeys);
        }
        return fullKeys;
    }

    public String enterName () {
        String fullKeys = "";
        if (StdDraw.hasNextKeyTyped()) {
            while (true) {
                Character ignoredKey = StdDraw.nextKeyTyped();
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                Font fontBig = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                StdDraw.text(WIDTH / 2, HEIGHT / 2, fullKeys);
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                StdDraw.show();
                break;
            }
        }
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character key = StdDraw.nextKeyTyped();
                String tempKey = Character.toString(key);
                if (!tempKey.equals(" ")) {
                    fullKeys += tempKey;
                }
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                Font fontBig = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(fontBig);
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Enter the Name");
                StdDraw.text(WIDTH / 2, HEIGHT / 2, fullKeys);
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Click Here and Press Enter to Continue");
                StdDraw.show();
            }
            if (!fullKeys.equals("") && StdDraw.isMousePressed()) {
                System.out.println("?");
                break;
            }
        }
        System.out.println("..");
        startSeed = true;
        return fullKeys;
    }


    // world generation
    public ArrayList<int[]> randomRoom(Random RANDOM) {
        ArrayList<int[]> rooms = new ArrayList<>();
        int roomNumber = RANDOM.nextInt(10) + 10;
        for (int i = 0; i < roomNumber; i++) {
            int x = RANDOM.nextInt(WIDTH - 9);
            int y = RANDOM.nextInt(HEIGHT - 9);
            int height = RANDOM.nextInt(7) + 2;
            int length = RANDOM.nextInt(7) + 2;
            int hallwayWidth = RANDOM.nextInt(2);
            int[] room = {x + 1, y + 1, hallwayWidth};
            int[] room2 = {x + 1, y + 1, height, length, hallwayWidth};
            rooms.add(room);
            for (int m = x + 1; m <= x + length; m++) {
                for (int n = y + 1; n <= y + height; n++) {
                    tileSet[m][n] = Tileset.FLOOR;
                }
            }
        }
        return rooms;
    }

    public void randomHallwayHelper(int currX, int targetX,
                                           int currY, int targetY) {
        if (currX < targetX) {
            hallwayOne(currX, targetX, currY, 0);
            if (currY < targetY) {
                hallwayOne(currY, targetY, targetX, 1);
            } else if (currY > targetY) {
                hallwayOne(targetY, currY, targetX, 1);
            }
        } else if (currX > targetX) {
            hallwayOne(targetX, currX, currY, 0);
            if (currY < targetY) {
                hallwayOne(currY, targetY, targetX, 1);
            } else if (currY > targetY) {
                hallwayOne(targetY, currY, targetX, 1);
            }
        }
    }

    public void randomHallway(ArrayList<int[]> rooms) {
        for (int i = 0; i < rooms.size() - 1; i++) {
            int[] room = rooms.get(i);
            int[] nextRoom = rooms.get(i + 1);
            int currX = room[0];
            int currY = room[1];
            int targetX = nextRoom[0];
            int targetY = nextRoom[1];
            int hallwayWidth = room[2] + 1;
            if (hallwayWidth == 1) {
                randomHallwayHelper(currX, targetX, currY, targetY);
            } else {
                randomHallwayHelper(currX, targetX + 1, currY, targetY);
                randomHallwayHelper(currX, targetX, currY + 1, targetY);
            }
        }
    }

    // direction=0 -> horizontal; direction=1 -> vertical
    public void hallwayOne(int start, int end, int otherCoordinate, int direction) {
        if (direction == 0) {
            for (int i = start; i < end; i++) {
                tileSet[i][otherCoordinate] = Tileset.FLOOR;
            }
        } else {
            for (int j = start; j < end; j++) {
                tileSet[otherCoordinate][j] = Tileset.FLOOR;
            }
        }
    }

    public void walls(int world_width, int world_height) {
        int count = 0;
        for (int i = 1; i < world_width - 1; i++) {
            for (int j = 1; j < world_height - 1; j++) {
                if (tileSet[i][j] == Tileset.FLOOR) {
                    count++;
                    if(count == 1){
                        tileSet[i][j] = Tileset.AVATAR;
                        position[0] = i;
                        position[1] = j;
                    }
                    if (tileSet[i][j + 1] == Tileset.NOTHING) {
                        tileSet[i][j + 1] = Tileset.WALL;
                    }
                    if (tileSet[i][j - 1] == Tileset.NOTHING) {
                        tileSet[i][j - 1] = Tileset.WALL;
                    }
                    if (tileSet[i + 1][j] == Tileset.NOTHING) {
                        tileSet[i + 1][j] = Tileset.WALL;
                    }
                    if (tileSet[i - 1][j] == Tileset.NOTHING) {
                        tileSet[i - 1][j] = Tileset.WALL;
                    }
                    if (tileSet[i - 1][j - 1] == Tileset.NOTHING) {
                        tileSet[i - 1][j - 1] = Tileset.WALL;
                    }
                    if (tileSet[i - 1][j + 1] == Tileset.NOTHING) {
                        tileSet[i - 1][j + 1] = Tileset.WALL;
                    }
                    if (tileSet[i + 1][j + 1] == Tileset.NOTHING) {
                        tileSet[i + 1][j + 1] = Tileset.WALL;
                    }
                    if (tileSet[i + 1][j - 1] == Tileset.NOTHING) {
                        tileSet[i + 1][j - 1] = Tileset.WALL;
                    }
                }
            }
        }
    }


    public void left() {
        int x = position[0];
        int y = position[1];
        if(tileSet[x - 1][y] == Tileset.FLOOR) {
            tileSet[x - 1][y] = Tileset.AVATAR;
            tileSet[x][y] = Tileset.FLOOR;
            position[0] = x - 1;
            //String file = "hitWall.wav";
            //double[] sample = StdAudio.read(file);
        }
        ter.renderFrame(tileSet);
    }

    public void right() {
        int x = position[0];
        int y = position[1];
        if(tileSet[x + 1][y] == Tileset.FLOOR) {
            tileSet[x + 1][y] = Tileset.AVATAR;
            tileSet[x][y] = Tileset.FLOOR;
            position[0] = x + 1;
            //String file = "hitWall.wav";
            //double[] sample = StdAudio.read(file);
            //System.out.print(sample.toString());
            //StdAudio.play(sample);
        }
        ter.renderFrame(tileSet);
    }

    public void up() {
        int x = position[0];
        int y = position[1];
        if(tileSet[x][y + 1] == Tileset.FLOOR) {
            tileSet[x][y + 1] = Tileset.AVATAR;
            tileSet[x][y] = Tileset.FLOOR;
            position[1] = y + 1;
            //String file = "hitWall.wav";
            //double[] sample = StdAudio.read(file);
            //StdAudio.play(sample);
        }
        ter.renderFrame(tileSet);
    }

    public void down() {
        int x = position[0];
        int y = position[1];
        if(tileSet[x][y - 1] == Tileset.FLOOR) {
            tileSet[x][y - 1] = Tileset.AVATAR;
            tileSet[x][y] = Tileset.FLOOR;
            position[1] = y - 1;
            //String file = "hitWall.wav";
            //double[] sample = StdAudio.read(file);
            //StdAudio.play(sample);
        }
        ter.renderFrame(tileSet);
    }

}
