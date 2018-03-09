package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;

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

        cityLevel(stage, city);
        population(stage, city);
        happiness(stage, city);

        for (UiElement element : statusWindow.getElementList()) {
            element.setHeight(statusWindow.getElementList().get(0).getHeight() + 10);
        }
        statusWindow.orderAllNeatly(1);
        statusWindow.updateElements();
    }


    private void cityLevel(final Stage stage, StructureCity city) {
        UILabel label = new UILabel(5, 0, 0, 0, 32, "City Level " + (city.getLevel() + 1));
        statusWindow.add(label, stage);
    }

    private void population(final Stage stage, StructureCity city) {
        UILabel label = new UILabel(5, 0, 0, 0, 32, "Population: " + city.getPopulation());
        statusWindow.add(label, stage);
    }

    private void happiness(final Stage stage, StructureCity city) {
        UILabel label = new UILabel(5, 0, 0, 0, 32, "Happiness: "
                + (city.getHappiness()*100) + "%");
        if (city.getHappiness() >= 0.9) {
            label.getLabel().getStyle().fontColor = Color.GREEN;
        } else if (city.getHappiness() >= 0.7) {
            label.getLabel().getStyle().fontColor = Color.LIME;
        } else if (city.getHappiness() >= 0.5) {
            label.getLabel().getStyle().fontColor = Color.YELLOW;
        } else if (city.getHappiness() >= 0.3) {
            label.getLabel().getStyle().fontColor = Color.ORANGE;
        } else {
            label.getLabel().getStyle().fontColor = Color.RED;
        }
        statusWindow.add(label, stage);
    }

}
