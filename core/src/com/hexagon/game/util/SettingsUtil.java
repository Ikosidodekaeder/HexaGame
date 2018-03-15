package com.hexagon.game.util;

import com.hexagon.game.map.tiles.Chunk;

/**
 * Created by Sven on 15.03.2018.
 */

public class SettingsUtil {

    private static boolean lowGraphics = false;

    public static boolean isLowGraphics() {
        return lowGraphics;
    }

    public static void setLowGraphics(boolean lowGraphics) {
        SettingsUtil.lowGraphics = lowGraphics;
        if (lowGraphics) {
            Chunk.RANGE_X = 7;
            Chunk.RANGE_Z_1 = 2;
            Chunk.RANGE_Z_2 = 10;
        } else {
            Chunk.RANGE_X = 22;
            Chunk.RANGE_Z_1 = 4;
            Chunk.RANGE_Z_2 = 19;
        }
    }
}
