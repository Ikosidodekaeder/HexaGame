package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Biome;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketBuild;
import com.hexagon.game.network.packets.PacketDestroy;

/**
 * Created by Sven on 01.03.2018.
 */

public class SidebarBuild extends Sidebar {

    public SidebarBuild(GroupWindow window, Stage stage) {
        super(window, stage);
    }

    @Override
    public void select(final HexMap map, final Point p, final Stage stage) {
        super.select(map, p, stage);
        statusWindow.removeAll(stage);

        Tile tile = map.getTileAt(p.getX(), p.getY());
        if (tile.getStructure() != null) {
            Structure structure = tile.getStructure();
            switch (structure.getType()) {
                case FOREST:
                    destroyForestButton(p, stage);
                    break;
                case ORE:
                    destroyMine(p,stage);
                    break;
                case CITY:
                    // TODO: Open City Information Window
                default:
                    break;
            }
        } else {
            if (tile.getBiome() == Biome.PLAINS) {
                addForestButton(p, stage);
                addMine(p,stage);
            }
        }
        statusWindow.orderAllNeatly(1);
        statusWindow.updateElements();
    }

    private void destroyForestButton(final Point p, final Stage stage) {
        UiButton buttonForest = new UiButton("Remove Forest", 5, 0, 50, 0, 26);
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

    private void addForestButton(final Point p, final Stage stage) {
        UiButton buttonForest = new UiButton("Add Forest", 5, -50, 50, 0, 26);
        buttonForest.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketBuild(p, StructureType.FOREST, HexaServer.senderId)
                );

                select(GameManager.instance.getGame().getCurrentMap(), p, stage);
            }
        });
        statusWindow.add(buttonForest, stage);
    }

    private void destroyMine(final Point p, final Stage stage) {
        UiButton Mine = new UiButton("Remove Mine", 5, 0, 50, 0, 26);
        Mine.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketDestroy(p)
                );
                select(GameManager.instance.getGame().getCurrentMap(), p, stage);
            }
        });
        statusWindow.add(Mine, stage);

    }

    private void addMine(final Point p, final Stage stage) {
        UiButton Mine = new UiButton("Add Mine", 5, 0, 50, 0, 26);
        Mine.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketBuild(p, StructureType.MINE, HexaServer.senderId)
                );
                select(GameManager.instance.getGame().getCurrentMap(), p, stage);

            }
        });
        statusWindow.add(Mine, stage);
    }
}
