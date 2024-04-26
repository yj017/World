import core.AutograderBuddy;
import core.Avatar;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;

import static com.google.common.truth.Truth.assertThat;

public class WorldGenTests {
    @Test
    public void basicTest() {
        // put different seeds here to test different worlds
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n2913037645193632194s");

        TERenderer ter = new TERenderer();
        ter.initialize(tiles.length, tiles[0].length);
        ter.renderFrame(tiles);
        StdDraw.pause(5000); // pause for 5 seconds so you can see the output
    }

    @Test
    public void basicInteractivityTest() {
        // TODO: write a test that uses an input like "n123swasdwasd"
        TETile[][] tiles1 = AutograderBuddy.getWorldFromInput("n5643591630821615871swwaaws");
        TERenderer ter = new TERenderer();
        ter.initialize(tiles1.length, tiles1[0].length);
        ter.renderFrame(tiles1);
        StdDraw.pause(50000000);
    }

    @Test
    public void basicSaveTest() {
        // TODO: write a test that calls getWorldFromInput twice, with "n123swasd:q" and with "lwasd"
        TETile[][] tiles1 = AutograderBuddy.getWorldFromInput("n123swasdddddd:q");
//        TETile[][] tiles2 = AutograderBuddy.getWorldFromInput("lw");

        TETile[][] tiles2 = AutograderBuddy.getWorldFromInput("lwasd");
        TETile[][] tiles3 = AutograderBuddy.getWorldFromInput("n123swasdwasd");

        TERenderer ter = new TERenderer();

        ter.initialize(tiles1.length, tiles1[0].length);
        ter.renderFrame(tiles1);
        StdDraw.pause(50000000);

        ter.initialize(tiles2.length, tiles2[0].length);
        ter.renderFrame(tiles2);
        StdDraw.pause(50000000);

        ter.initialize(tiles3.length, tiles3[0].length);
        ter.renderFrame(tiles3);
        StdDraw.pause(50000000);

        assertThat(tiles2).isEqualTo(tiles3);
    }

    @Test
    public void loopwhileAreaNowMinTest() { // timed out error test
        AutograderBuddy.getWorldFromInput("n2913037645193632194s");
    }

    @Test
    public void customAvatarSaveTest() {
        TETile[][] tiles1 = AutograderBuddy.getWorldFromInput("b1121231234sssssssaaaaaaaaaaaaaaaaaaaaaaass:q");
        TERenderer ter = new TERenderer();
        ter.initialize(tiles1.length, tiles1[0].length);
        ter.renderFrame(tiles1);
        StdDraw.pause(50000000);
    }
}
