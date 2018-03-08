package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.graphics.ui.UpdateEvent;
import com.hexagon.game.graphics.ui.windows.GroupWindow;

/**
 * Created by Sven on 01.03.2018.
 */

public class SidebarResources extends Sidebar {

    public final UILabel OreResource = new UILabel(0,0,200,0,28,"Ore: 0");
    public final UILabel WoodResource = new UILabel(0,0,200,0,28,"Wood: 0");
    public final UILabel StoneResource = new UILabel(0,0,200,0,28,"Stone: 0");

    public SidebarResources(GroupWindow window, Stage stage) {
        super(window, stage, 5, 5, Gdx.graphics.getHeight()/2-5);

        OreResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                OreResource.getLabel().setText("Ore: " + GameManager.instance.getPlayerResources().get(HexaComponents.ORE.name()));
            }
        });

        WoodResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                WoodResource.getLabel().setText("Wood: " + GameManager.instance.getPlayerResources().get(HexaComponents.WOOD.name()));
            }
        });

        StoneResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                StoneResource.getLabel().setText("Stone: " + GameManager.instance.getPlayerResources().get(HexaComponents.STONE.name()));
            }
        });


        statusWindow.add(OreResource, stage);
        statusWindow.add(WoodResource, stage);
        statusWindow.add(StoneResource, stage);
        for (UiElement element : statusWindow.getElementList()) {
            element.setHeight(element.getHeight() + 10);
            element.setX(5);
        }
        statusWindow.orderAllNeatly(1);
        statusWindow.updateElements();
    }

}
