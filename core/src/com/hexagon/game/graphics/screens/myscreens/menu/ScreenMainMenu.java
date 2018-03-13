package com.hexagon.game.graphics.screens.myscreens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
import com.hexagon.game.graphics.ui.windows.WindowNotification;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketRegister;
import com.hexagon.game.util.MenuUtil;

/**
 * Created by Sven on 14.12.2017.
 */

/**
 * Displays the first two Menus visible to the User
 */

/**
 * Concrete implementation of an Screen.
 * according to the constructor of type >MAIN_MENU<
 */
public class ScreenMainMenu extends ScreenMenuSuper {

    private SpriteBatch batch;
    private BitmapFont font;

    public ScreenMainMenu() {
        super(ScreenType.MAIN_MENU);
    }

    @Override
    public void create() {
        super.create();
        batch = new SpriteBatch();
        font = new BitmapFont();

        UILabel label = new UILabel(50, Gdx.graphics.getHeight() - 50, 100, 50, 36, "Your Username: " + HexaServer.username);

        final DropdownScrollableWindow window = new DropdownScrollableWindow(20, 0, 0, 0, 0, 0, 15);
        windowManager.addWindow(window);

        /*for (int i=0; i<400; i++) {
            UiButton buttonWindow = new UiButton(String.valueOf(i), 0, 0, 50, 20);
            window.add(buttonWindow, stage);
        }*/

        window.orderAllNeatly(13, 0, 15);
        window.setY(label.getY() - window.getHeight());
        window.setX(label.getX());
        window.updateElements();

        /*UiButton buttonWindow = new UiButton("Inside Window Button", 0, 0, 100, 50);
        buttonWindow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Clicked inside Window Button");
            }
        });
        window.add(buttonWindow, stage);*/

        label.addToStage(stage);


        //MenuUtil.getInstance().createStandardMenu(this);
        final GroupWindow standardWindow = new GroupWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);

        FadeWindow fadeWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);
        fadeWindow.show(stage);
        fadeWindow.add(new UiImage(0, 0, 224, 600, "sidebar.png"), stage);



        standardWindow.getWindowList().add(fadeWindow);

        /*
         * Subwindow 1: Play
         */
        final FadeWindow subwindowPlay = new FadeWindow(fadeWindow.getX() + fadeWindow.getWidth() + 10, fadeWindow.getY(), 800 - fadeWindow.getWidth(), 600, stage);
        subwindowPlay.add(new UiImage(0, 0, 558, 600, "window_small.png"), stage);

        UILabel playText = new UILabel(20, subwindowPlay.getHeight() - 60, 100, 40, 40, "Play");
        UiButton playHost = new UiButton("Host Game", 40, subwindowPlay.getHeight() - 100, 200, 40);
        UiButton playJoin = new UiButton("Join Game", 40, subwindowPlay.getHeight() - 140, 200, 40);
        UiButton playSingleplayer = new UiButton("Play Offline", 40, subwindowPlay.getHeight() - 180, 200, 40);

        playHost.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager gameManager = GameManager.instance;
                new WindowNotification("Connecting...", stage, windowManager);
                gameManager.connect(true);
                windowManager.removeNotifications(stage);
                new WindowNotification("Registering you as a Host...", stage, windowManager);
                gameManager.server.send(new PacketRegister(
                        HexaServer.senderId, // This is the host id
                        "Raum " + ((int) (Math.random()*100)+1),
                        false
                ));
            }
        });

        playJoin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.JOIN);
            }
        });

        playSingleplayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.GENERATOR);
            }
        });

        subwindowPlay.add(playText, stage);
        subwindowPlay.add(playHost, stage);
        subwindowPlay.add(playJoin, stage);
        subwindowPlay.add(playSingleplayer, stage);

        subwindowPlay.updateElements();
        standardWindow.getWindowList().add(subwindowPlay);

         /*
         * Subwindow 2
         */
        final FadeWindow subwindow2 = new FadeWindow(fadeWindow.getX() + fadeWindow.getWidth() + 10, fadeWindow.getY(), 800 - fadeWindow.getWidth(), 600, stage);
        subwindow2.add(new UiImage(0, 0, 558, 600, "window_small.png"), stage);

        UiButton text2 = new UiButton("This is subwindow 2", 40, subwindow2.getHeight() - 60, 100, 40);
        subwindow2.add(text2, stage);

        subwindow2.updateElements();
        standardWindow.getWindowList().add(subwindow2);

        /*
         * Sidebar Buttons
         */

        UiButton buttonPlay = new UiButton("Play", 20, fadeWindow.getHeight() - 60, 50, 40);
        UiButton buttonSubwindow2 = new UiButton("Subwindow 2", 20, buttonPlay.getY() - 50, 50, 40);

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (subwindowPlay.isVisible()) {
                    standardWindow.hide(subwindowPlay, stage);
                } else {
                    standardWindow.show(subwindowPlay, stage);
                }
            }
        });

        buttonSubwindow2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (subwindow2.isVisible()) {
                    standardWindow.hide(subwindow2, stage);
                } else {
                    standardWindow.show(subwindow2, stage);
                }
            }
        });

        fadeWindow.add(buttonPlay, stage);
        fadeWindow.add(buttonSubwindow2, stage);

        fadeWindow.updateElements();

        this.windowManager.addWindow(standardWindow);

        /*HexaServer hexaServer = new HexaServer("localhost", 25565, true);
        try {
            hexaServer.connect(1000);
            hexaServer.send(new PacketKeepAlive(1234));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        font.draw(batch, "Main menu", 20, 20);
        batch.end();


        stage.draw();

        Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        shapeRenderer.begin();
        windowManager.render(shapeRenderer);
        shapeRenderer.end();

        if (GameManager.instance.server != null) {
            GameManager.instance.server.callEvents();
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
}
