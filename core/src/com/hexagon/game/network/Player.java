package com.hexagon.game.network;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Sven on 08.03.2018.
 */

public class Player {

    public Color color;
    public String username;

    public Player(Color color, String username) {
        this.color = color;
        this.username = username;
    }
}