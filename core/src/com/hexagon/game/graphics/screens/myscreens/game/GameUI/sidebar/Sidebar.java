package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.windows.AnimationWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;

/**
 * Created by Johannes on 25.02.2018.
 */

public class Sidebar {

    public AnimationWindow statusWindow;
    Stage stage;


    public Sidebar(GroupWindow window, Stage stage, int y, int y2, int height) {
        statusWindow = new AnimationWindow(-355, y, 5, y2, 350, height, stage);
        window.getWindowList().add(statusWindow);

        this.stage = stage;
    }

    public void select(HexMap map, Point p, Stage stage) {
        statusWindow.show(stage);
    }

    public void deselect(Stage stage) {
        statusWindow.hide(stage);
    }


}
