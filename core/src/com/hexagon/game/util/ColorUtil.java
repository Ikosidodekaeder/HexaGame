package com.hexagon.game.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Sven on 05.03.2018.
 */

public class ColorUtil {

    private Color[] colors = new Color[] {
            Color.ORANGE,
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.WHITE,
            Color.SKY
    };
    private int i = 0;

    public Color getNext() {
        if (i >= colors.length) {
            return new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
        }
        Color next = colors[i];
        i++;
        return next;
    }
}
