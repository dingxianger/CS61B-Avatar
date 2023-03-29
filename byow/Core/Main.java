package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        /*
        boolean run = false;
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else {
            Engine engine = new Engine();
            if (args.length == 2 && args[0].equals("-s")) {
                TETile[][] tileSet = engine.interactWithInputString(args[1]);
                engine.ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                engine.ter.renderFrame(tileSet);
                run = true;
            }
            while (run == true) {
                engine.interactWithKeyboard();
            }
        }

         */
        Engine engine = new Engine();
        engine.interactWithKeyboard();

    }

}
