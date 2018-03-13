package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketBuild;
import com.hexagon.game.network.packets.PacketDestroy;

/**
 * Created by Sven on 01.03.2018.
 */

public class SidebarBuild extends Sidebar {

    public SidebarBuild(GroupWindow window, Stage stage) {
        super(window, stage, Gdx.graphics.getHeight()/2+5, Gdx.graphics.getHeight()/2+5, Gdx.graphics.getHeight()/2-60);
    }

    @Override
    public void select(final HexMap map, final Point p, final Stage stage) {
        Tile tile = map.getTileAt(p.getX(), p.getY());
        if (tile.getOwner() != null
                && !tile.getOwner().equals(HexaServer.senderId)) {
            GameManager.instance.messageUtil.add("You don't own this tile!", 4000, Color.RED);
            return;
        }

        super.select(map, p, stage);
        statusWindow.removeAll(stage);

        if (tile.getStructure() != null) {
            Structure structure = (Structure) tile.getStructure();
            switch (structure.getType()) {
                case MINE:
                    destroyMine(p,stage);
                    break;
                case FOREST:
                    addForestryButton(p, stage);
                    break;
                case FORESTRY:
                    destroyForestryButton(p,stage);
                    break;
                case QUARRY:
                    destroyQuarry(p,stage);
                    break;
                case ORE:
                    addMine(p,stage);
                    break;
                default:
                    break;
            }
        } else {
            switch (tile.getBiome()){
                case PLAINS:{
                    //addForestryButton(p, stage);
                    //addQuarry(p,stage);
                    addFactoryButton(p, stage);
                }break;
                case DESERT:{

                }break;
                case ICE:{

                }break;

            }
        }
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

    private void addForestryButton(final Point p, final Stage stage) {
        UiButton buttonForest = new UiButton("Build Forestry", 5, 0, 50, 0, 26);
        buttonForest.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketBuild(p, StructureType.FORESTRY, HexaServer.senderId)
                );

                select(GameManager.instance.getGame().getCurrentMap(), p, stage);
            }
        });
        statusWindow.add(buttonForest, stage);
    }

    private void destroyFactoryButton(final Point p, final Stage stage) {
        UiButton buttonFactory = new UiButton("Destroy Factory", 5, 0, 50, 0, 26);
        buttonFactory.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketDestroy(p)
                );
                select(GameManager.instance.getGame().getCurrentMap(), p, stage);
            }
        });
        statusWindow.add(buttonFactory, stage);

    }

    private void addFactoryButton(final Point p, final Stage stage) {
        UiButton buttonFactory = new UiButton("Build Factory", 5, 0, 50, 0, 26);
        buttonFactory.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketBuild(p, StructureType.FACTORY, HexaServer.senderId)
                );

                select(GameManager.instance.getGame().getCurrentMap(), p, stage);
            }
        });
        statusWindow.add(buttonFactory, stage);
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


    private void destroyQuarry(final Point p, final Stage stage) {
        UiButton Mine = new UiButton("Remove Quarry", 5, 0, 50, 0, 26);
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

    private void addQuarry(final Point p, final Stage stage) {
        UiButton Mine = new UiButton("Add Quarry", 5, 0, 50, 0, 26);
        Mine.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketBuild(p, StructureType.QUARRY, HexaServer.senderId)
                );
                select(GameManager.instance.getGame().getCurrentMap(), p, stage);

            }
        });
        statusWindow.add(Mine, stage);
    }

}
