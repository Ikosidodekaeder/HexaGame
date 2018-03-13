package com.hexagon.game.graphics.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.hexagon.game.Main;
import com.hexagon.game.graphics.screens.myscreens.DemoScreen;
import com.hexagon.game.graphics.screens.myscreens.ScreenGenerator;
import com.hexagon.game.graphics.screens.myscreens.menu.ScreenHost;
import com.hexagon.game.graphics.screens.myscreens.menu.ScreenJoin;
import com.hexagon.game.graphics.screens.myscreens.ScreenLoading;
import com.hexagon.game.graphics.screens.myscreens.menu.ScreenLobby;
import com.hexagon.game.graphics.screens.myscreens.menu.ScreenMainMenu;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;
import com.hexagon.game.graphics.ui.windows.WindowNotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 18.12.2017.
 */

public class ScreenManager {

    /**
     * Singleton instance of this class!!!
     */
    private static ScreenManager instance;

    /**
     * Holds all loaded screens
     */
    private List<HexagonScreen> screenList;

    /**
     * stores reference to the actual display which is shown to the user
     */
    private HexagonScreen currentScreen;


    /**
     * Constructor
     */
    public ScreenManager() {
        instance = this;

        screenList = new ArrayList<>();
        screenList.add(new ScreenLoading());
        screenList.add(new ScreenMainMenu());

        screenList.add(new ScreenHost());
        screenList.add(new ScreenJoin());
        screenList.add(new ScreenLobby());
        screenList.add(new ScreenGenerator());

        screenList.add(new ScreenGame());

        screenList.add(new DemoScreen());



        this.currentScreen = screenList.get(0); // loading screen
    }

    /**
     * Changes the currently displayed screen to the one specified
     * in ScreenType parameter.
     * @param type
     * @return
     */
    public HexagonScreen setCurrentScreen(ScreenType type) {
        for (HexagonScreen screen : screenList) {
            if (screen.getScreenType() == type) {
                this.currentScreen = screen;
                GameManager.instance.setStage(screen.getStage());
                Main.getInstance().setScreen(screen);
                return screen;
            }
        }
        // this shouldn't happen
        Gdx.app.error("Hexagon", "Screen null");
        Gdx.app.exit();
        return null;
    }

    public void checkLogout(ScreenType screenType) {
        if (GameManager.instance == null
                || GameManager.instance.server == null) {
            return;
        }
        switch (screenType) {
            case MAIN_MENU:
            case JOIN:
                if (GameManager.instance.server.disconnect()) {
                    new WindowNotification("You have disconnected from the server",
                            getCurrentScreen().getStage(),
                            getCurrentScreen().windowManager);
                }
                break;
        }
    }

    /**
     *
     * @return
     */
    public List<HexagonScreen> getScreenList() {
        return screenList;
    }

    /**
     * returns the currently displayed screen
     * @return
     */
    public HexagonScreen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Clears the screen in the specified colour
     *
     * @param red
     * @param green
     * @param blue
     */
    public void clearScreen(float red, float green, float blue) {
        Gdx.gl.glClearColor(red, green, blue, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     *
     * @return
     */
    public static ScreenManager getInstance() {
        return instance;
    }
}
