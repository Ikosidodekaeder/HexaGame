package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateCityView;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateType;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.DropdownWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.CityBuildings;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.network.packets.PacketCityBuild;

/**
 * Created by Sven on 01.03.2018.
 */

public class SidebarCity extends Sidebar {

    private StateCityView   owner;

    private DropdownWindow  costWindow;

    public SidebarCity(GroupWindow window, Stage stage, StateCityView owner) {
        super(window, stage, Gdx.graphics.getHeight()/2+5, Gdx.graphics.getHeight()/2+5, Gdx.graphics.getHeight()/2-60);
        this.owner = owner;

        costWindow = new DropdownWindow(0, 0, 400, 400, 0, 0);
        costWindow.hide(stage);

    }

    @Override
    public void select(final HexMap map, final Point p, final Stage stage) {
        super.select(map, p, stage);
        statusWindow.removeAll(stage);
        owner.getHoverWindow().removeAll(stage);

        Tile tile = map.getTileAt(p.getX(), p.getY());
        if (tile.getStructure() == null
                || tile.getStructure().getType() != StructureType.CITY) {
            return;
        }
        if (!(tile.getStructure() instanceof StructureCity)) {
            System.err.println("ERROR SidebarCity: Structure not an instance of StructureCity even though its type is CITY");
            return;
        }

        StructureCity city = (StructureCity) tile.getStructure();


        cityNameLevel(stage, city);

        exitCityView(stage);
        showCityBuildings(stage, city, map, p);

        for (UiElement element : statusWindow.getElementList()) {
            element.setHeight(28);
        }
        statusWindow.orderAllNeatly(1);
        statusWindow.updateElements();
    }

    @Override
    public void deselect(Stage stage) {
        super.deselect(stage);
        owner.getHoverWindow().hide(stage);
    }

    private void cityNameLevel(final Stage stage, StructureCity city) {
        UILabel labelName   = new UILabel(
                0, 0, 0, 0, 42, "" + city.getName(), Color.SKY, null
        );
        UILabel labelLevel  = new UILabel(
                0, -50, 0, 0, 42, "Level " + (city.getLevel() + 1), Color.SKY, null
        );
        labelName.setY(owner.getHoverWindow().getHeight() - labelName.getHeight()*1.5f);
        labelName.setX(owner.getHoverWindow().getWidth()/2  - labelName.getWidth()/2);

        labelLevel.setY(labelName.getY() - labelLevel.getHeight() - 10);
        labelLevel.setX(owner.getHoverWindow().getWidth()/2 - labelLevel.getWidth()/2);
        owner.getHoverWindow().add(labelName, stage);
        owner.getHoverWindow().add(labelLevel, stage);
        owner.getHoverWindow().show(stage);
    }


    private void exitCityView(Stage stage) {
        final UiButton exit = new UiButton("Exit City View", 5, 0, 50, 0, 32);
        exit.getTextButton().getStyle().fontColor = Color.RED;
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                owner.getGameManager().setCurrentState(StateType.MAIN_GAME);
            }
        });
        statusWindow.add(exit, stage);
        exit.setEnterEvent(new UiButton.EnterEvent() {
            @Override
            public void enter(UiButton button) {
                showCostAt(exit.getX(), exit.getY(), new String[]{"Close this view and", "return to the main view."});
            }
        });
    }

    private void showCityBuildings(final Stage stage, final StructureCity city, final HexMap map, final Point p) {
        for (final CityBuildings building : CityBuildings.values()) {
            final UiButton button;
            boolean alreadyBought = false;

            if (city.getCityBuildingsList().contains(building)) {
                alreadyBought = true;
                button = new UiButton(building.getFriendlyName(), 5, 0, 0, 0, 30);
                button.getTextButton().getStyle().fontColor = Color.GREEN;
            } else {
                if (city.getLevel() >= building.getMinLevel()) {
                    button = new UiButton(building.getFriendlyName() + "  " + building.getCost() + "$", 5, 0, 0, 0, 30);
                    button.getTextButton().getStyle().fontColor = Color.GRAY;
                } else {
                    button = new UiButton(building.getFriendlyName() + " (Level " + (building.getMinLevel()+1) + ")", 5, 0, 0, 0, 30);
                    button.getTextButton().getStyle().fontColor = Color.DARK_GRAY;
                }
            }

            statusWindow.add(button, stage);

            final boolean finalAlreadyBought = alreadyBought;
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (city.getLevel() < building.getMinLevel()
                            || finalAlreadyBought) {
                        return;
                    }
                    // check if the player has enough money to buy

                    GameManager.instance.server.send(
                            new PacketCityBuild(city.getArrayPosition(), building)
                    );
                }
            });
        }
    }

    private void showCostAt(float x, float y, String[] lines) {
        costWindow.setX(x);
        costWindow.setY(y);

        costWindow.removeAll(stage);
        for (String line : lines) {
            UILabel label = new UILabel(0, 0, 0, 0, 25, line);
            costWindow.add(label, stage);
        }
        costWindow.show(stage);
    }

}
