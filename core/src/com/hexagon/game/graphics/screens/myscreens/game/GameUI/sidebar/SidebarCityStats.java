package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.packets.PacketDestroy;

/**
 * Created by Sven on 01.03.2018.
 */

public class SidebarCityStats extends Sidebar {

    public SidebarCityStats(GroupWindow window, Stage stage) {
        super(window, stage, 5, 5, Gdx.graphics.getHeight()/2-5);
    }

    @Override
    public void select(final HexMap map, final Point p, final Stage stage) {
        super.select(map, p, stage);
        statusWindow.removeAll(stage);

        Tile tile = map.getTileAt(p.getX(), p.getY());
        if (tile.getStructure() == null
                || tile.getStructure().getType() != StructureType.CITY) {
            return;
        }
        if (!(tile.getStructure() instanceof StructureCity)) {
            System.err.println("ERROR SidebarCityStats: Structure not an instance of StructureCity even though its type is CITY");
            return;
        }

        StructureCity city = (StructureCity) tile.getStructure();

        statusWindow.orderAllNeatly(1);
        statusWindow.updateElements();
    }

    private void destroyForestryButton(final Point p, final Stage stage) {
        UiButton buttonForest = new UiButton("Remove Forestry", 5, 0, 50, 0, 26);
        buttonForest.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketDestroy(p)
                );
                select(GameManager.instance.getGame().getCurrentMap(), p, stage);
            }
        });
        statusWindow.add(buttonForest, stage);

    }

}
