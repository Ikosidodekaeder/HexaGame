package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UpdateEvent;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;

/**
 * Created by Johannes on 25.02.2018.
 */

public class StatusBar {

    public Window      Top = new Window(0,Gdx.graphics.getHeight()-50,Gdx.graphics.getWidth(),50);

    public float level = Top.getHeight()-50+10;
    public float rightEnd = Top.getWidth();
    public float ScreenWidth = Gdx.graphics.getWidth();
    public float ScreenHeight = Gdx.graphics.getHeight();

    public float MenuWidth = 200;
    public float MenuHeight = 200;
    public float StatusHeight = 50;


    public UILabel StatusInfos     = new UILabel(10,level,200,StatusHeight,32,"Test");
    public IngameMenu  MainMenu;



    public StatusBar(final Stage stage, GroupWindow window){
        MainMenu =  new IngameMenu(
                rightEnd,level,
                ScreenWidth/2-MenuWidth/2,ScreenHeight/2-MenuHeight/2,
                MenuWidth,MenuHeight,
                stage
        );

        StatusInfos.setUpdateEvent(new UpdateEvent(){

            @Override
            public void onUpdate() {
                StatusInfos.getLabel().setText("" + Gdx.graphics.getFramesPerSecond() + " FPS, " + ScreenGame.renderedTiles + " Tiles");
            }
        });


        Top.add(StatusInfos,stage);
        Top.add(MainMenu.Menu,stage);
        Top.updateElements();

        window.getWindowList().add(Top);
        window.getWindowList().add(MainMenu.MenuContent);
        window.show(stage);


    }


}
