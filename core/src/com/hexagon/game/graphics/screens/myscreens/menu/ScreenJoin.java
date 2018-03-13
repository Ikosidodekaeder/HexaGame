package com.hexagon.game.graphics.screens.myscreens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.DropdownScrollableWindow;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketJoin;
import com.hexagon.game.network.packets.PacketServerList;
import com.hexagon.game.util.MenuUtil;

import java.util.UUID;

/**
 * Created by Sven on 14.12.2017.
 */

/**
 * Displays a list of servers
 */
public class ScreenJoin extends ScreenMenuSuper {

    public DropdownScrollableWindow subwindowServers;

    public boolean joining = false;
    public Window messageBox;

    private SpriteBatch batch;
    private BitmapFont font;

    public ScreenJoin() {
        super(ScreenType.JOIN);
    }

    private void setupUserInterface(){

        if (messageBox != null) {
            messageBox.removeAll(stage);
            messageBox = null;
            joining = false;
        }
        windowManager.removeAll(stage);

        UiButton button = new UiButton("Back", 50, Gdx.graphics.getHeight() - 50, 100, 50);
        button.addToStage(stage);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
            }
        });


        //MenuUtil.getInstance().createStandardMenu(this);
        final GroupWindow standardWindow = new GroupWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);

        FadeWindow fadeWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);
        fadeWindow.show(stage);
        fadeWindow.add(new UiImage(0, 0, 224, 600, "sidebar.png"), stage);



        standardWindow.getWindowList().add(fadeWindow);

        /*
         * Subwindow 1: Servers
         */
        subwindowServers = new DropdownScrollableWindow(fadeWindow.getX() + fadeWindow.getWidth() + 10, fadeWindow.getY(), 800 - fadeWindow.getWidth(), 600, 5, 5, 6);
        subwindowServers.add(new UiImage(0, 0, 558, 600, "window_small.png"), stage);

        UiButton playersText    = new UiButton("Loading Rooms", 50, 0, 100, 40);


        subwindowServers.add(playersText, stage);

        subwindowServers.orderAllNeatly(1);

        subwindowServers.updateElements();
        standardWindow.getWindowList().add(subwindowServers);

        /*
         * Sidebar Buttons
         */

        UiButton buttonServers = new UiButton("Servers", 20, fadeWindow.getHeight() - 60, 50, 40);
        //UiButton buttonHostGame = new UiButton("Host Game", 20, buttonServers.getY() - 80, 50, 40);

        buttonServers.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (subwindowServers.isVisible()) {
                    standardWindow.hide(subwindowServers, stage);

                } else {
                    standardWindow.show(subwindowServers, stage);
                    GameManager.instance.server.send(
                            new PacketServerList() // Requests a list of servers
                    );
                }
            }
        });

        /*buttonHostGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.HOST);
            }
        });*/

        fadeWindow.add(buttonServers, stage);
        //fadeWindow.add(buttonHostGame, stage);

        fadeWindow.updateElements();

        this.windowManager.addWindow(standardWindow);
    }

    @Override
    public void create() {
        super.create();
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void show() {
        super.show();

        setupUserInterface();

        GameManager gameManager = GameManager.instance;
        gameManager.connect(false);
    }

    private float callEventsTime = 0;
    private void update(float delta) {
        if (GameManager.instance.server != null) {

            callEventsTime += delta;
            if (callEventsTime >= 1.0) {
                GameManager.instance.server.callEvents();
                callEventsTime = 0;
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        this.update(delta);

        batch.begin();
        font.draw(batch, "Join a game", 20, 20);
        batch.end();

        if (!joining && messageBox != null && messageBox.isVisible()) {
            messageBox.hide(getStage());
        }


        if (joining) {
            shapeRenderer.begin();
            windowManager.render(shapeRenderer);
            shapeRenderer.end();
        }
        stage.draw();
        if (messageBox != null) {
            shapeRenderer.begin();
            messageBox.render(shapeRenderer);
            shapeRenderer.end();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
        font.dispose();
        stage.dispose();
        batch.dispose();
    }

    public void joinRoom(UUID hostUuid, String roomName) {
        if (joining) {
            return;
        }
        joining = true;
        if (messageBox != null) {
            messageBox.removeLabels(getStage());
            //windowManager.remove(stage, messageBox);
        } else {
            messageBox = new Window(MenuUtil.getInstance().getX() + 50, MenuUtil.getInstance().getY() + 50, 800 - 100, 600 - 100);
            messageBox.setPriority(100);
            //windowManager.addWindow(messageBox);
        }

        UILabel label = new UILabel(50, 50, 300, 100,32, "Joining room " + roomName + "...");
        messageBox.add(label, getStage());

        messageBox.show(getStage());

        GameManager.instance.server.send(
                new PacketJoin(HexaServer.username, hostUuid, HexaServer.VERSION)
        );
    }
}
