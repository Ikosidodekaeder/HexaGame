package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UpdateEvent;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.Player;

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


    public UILabel labelClaims = new UILabel(10,level,200,StatusHeight,32,"Claims: ?");
    public UILabel labelMoney = new UILabel(180,level,200,StatusHeight,30,"Money: 0");
    public UILabel labelPopulation = new UILabel(380,level,200,StatusHeight,30,"Population: 0");
    public UILabel labelUnemployed = new UILabel(650,level,200,StatusHeight,30,"Unemployed: 0");

    public final UILabel PlayerID = new UILabel(875,level,200,StatusHeight,16,GameManager.instance.server.getLocalClientID().toString());
    public IngameMenu  MainMenu;



    public StatusBar(final Stage stage, GroupWindow window){
        MainMenu =  new IngameMenu(
                rightEnd,level,
                ScreenWidth/2-MenuWidth/2,ScreenHeight/2-MenuHeight/2,
                MenuWidth,MenuHeight,
                stage
        );

        labelClaims.setUpdateEvent(new UpdateEvent(){

            @Override
            public void onUpdate() {
                labelClaims.getLabel().setText(
                        GameManager.instance.server.getSessionData().PlayerList.get(HexaServer.senderId).getSecond().claims + " Claims"
                );
            }
        });

        labelMoney.setUpdateEvent(new UpdateEvent(){

            @Override
            public void onUpdate() {
                labelMoney.getLabel().setText(
                        "Money: " + GameManager.instance.server.getSessionData().PlayerList.get(HexaServer.senderId).getSecond().claims
                );
            }
        });

        labelPopulation.setUpdateEvent(new UpdateEvent(){

            @Override
            public void onUpdate() {
                labelPopulation.getLabel().setText(
                        "Population: " + GameManager.instance.server.getSessionData().PlayerList.get(HexaServer.senderId).getSecond().population
                );
            }
        });

        labelUnemployed.setUpdateEvent(new UpdateEvent(){

            @Override
            public void onUpdate() {
                Player player = GameManager.instance.server.getSessionData().PlayerList.get(HexaServer.senderId).getSecond();
                int unemployed = player.population - player.jobs;
                if (unemployed < 0) {
                    labelUnemployed.getLabel().getStyle().fontColor = Color.GRAY;
                    labelUnemployed.getLabel().setText(
                            "Jobs: " + Math.abs(unemployed)
                    );
                    return;
                }
                if (unemployed > 500) {
                    labelUnemployed.getLabel().getStyle().fontColor = Color.RED;
                } else if (unemployed > 100) {
                    labelUnemployed.getLabel().getStyle().fontColor = Color.ORANGE;
                } else if (unemployed < 10) {
                    labelUnemployed.getLabel().getStyle().fontColor = Color.GREEN;
                } else {
                    labelUnemployed.getLabel().getStyle().fontColor = Color.WHITE;
                }
                labelUnemployed.getLabel().setText(
                        "Unemployed: " + unemployed
                );
            }
        });



        PlayerID.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                PlayerID.getLabel().setText(GameManager.instance.server.getLocalClientID().toString() + (GameManager.instance.server.isHost() ? "  Host" : "  Client"));
            }
        });

        Top.add(labelClaims,stage);
        Top.add(labelMoney,stage);
        Top.add(labelPopulation,stage);
        Top.add(labelUnemployed,stage);
        Top.add(PlayerID,stage);
        Top.add(MainMenu.Menu,stage);
        Top.updateElements();

        window.getWindowList().add(Top);
        window.getWindowList().add(MainMenu.MenuContent);
        window.show(stage);


    }




}
