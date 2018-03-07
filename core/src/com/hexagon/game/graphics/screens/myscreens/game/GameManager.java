package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.State;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateCityView;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateGameOver;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateMainGame;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateStartOfGame;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateType;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.MessageUtil;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.StatusBar;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.TileInfoField;
import com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar.SidebarBuild;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.WindowNotification;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.util.ColorUtil;
import com.hexagon.game.util.MenuUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Sven on 16.02.2018.
 */

public class GameManager {

    public static GameManager instance;

    private ScreenGame      game;
    private WindowManager   windowManager;
    private Stage           stage;
    private InputGame       inputGame;
    public  ColorUtil       colorUtil;
    public  MessageUtil     messageUtil;

    public List<State>      states = new ArrayList<>();
    public State            currentState;

    private Map<String,Integer> PlayerResources = new Hashtable<String,Integer>() {{
        put("STONE",0);
        put("WOOD",0);
        put("ORE",-10);
    }};

    ShapeRenderer           shapeRenderer;
    GroupWindow             standardWindow;
    FadeWindow              spaceWindow;

    public SidebarBuild     sidebarBuildWindow;


    public HexaServer       server;

    public GameManager() {
        instance = this;

        standardWindow = new GroupWindow(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),stage);

    }

    /*
     * Game States
     */

    public void createStates() {
        if (states != null) {
            for (State state : states) {
                state.hide();
            }
        }
        states = new ArrayList<>();
        states.add(new StateStartOfGame(inputGame, this));
        states.add(new StateMainGame(inputGame, this));
        states.add(new StateCityView(inputGame, this));
        states.add(new StateGameOver(inputGame, this));
    }

    public void setCurrentState(StateType type) {
        if (currentState != null) currentState.hide();

        for (State state : states) {
            if (state.getStateType() == type) {
                currentState = state;
                currentState.show();
                return;
            }
        }
        throw new IllegalArgumentException("No State found for StateType " + type.name());
    }

    public State getCurrentState(){
        return currentState;
    }

    /*
     *
     */



    public void startGame(ScreenGame game) {
        this.game = game;
        this.windowManager = game.getWindowManager();
        this.stage = game.getStage();
        this.inputGame = game.getInputGame();
        if (messageUtil != null) {
            messageUtil.removeAll();
        }
        this.messageUtil = new MessageUtil(stage, windowManager);

        createStates();

        setCurrentState(StateType.START_OF_GAME);
    }

    public void playOffline() {
        server = new HexaServer(
                null
                , 0
        );

        colorUtil = new ColorUtil();
        server.hostOffline();
    }

    public void connect(boolean isHost) {
        server = new HexaServer(
                "svdragster.dtdns.net",
                //"localhost",
                25565,
                isHost
        );

        colorUtil = new ColorUtil();

        if (isHost) {
            //HexaServer.senderId = UUID.fromString("a84223f7-f8dd-4ea4-8494-25ef9d27a1a1");
            server.getSessionData().addNewPlayer(HexaServer.senderId,"HOST",
                    colorUtil.getNext());
        } else {
            //HexaServer.senderId = UUID.fromString("a25183d9-1a5a-40e1-a712-e3099282c349");
        }



        try {
            server.connect(10_000);
        } catch (IOException e) {
            System.out.println("Could not connect!");
            e.printStackTrace();
            ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
            new WindowNotification("Could not connect to the Server",
                    ScreenManager.getInstance().getCurrentScreen().getStage(),
                    ScreenManager.getInstance().getCurrentScreen().getWindowManager());
        }
    }

    public void createUserInterface() {
        shapeRenderer = new ShapeRenderer();
        createButtons();
        createWindows();
    }

    public void createButtons() {
        /*UiButton button = new UiButton("Hello World", 50, Gdx.graphics.getHeight() - 50, 100, 50);

        final DropdownScrollableWindow window = new DropdownScrollableWindow(20, 0, 0, 0, 0, 0, 15);
        windowManager.addWindow(window);

        for (int i=0; i<20; i++) {
            UiButton buttonWindow = new UiButton(String.valueOf(i), 0, 0, 50, 25);
            window.add(buttonWindow, stage);
        }

        window.orderAllNeatly(13, 0, 15);
        window.setY(button.getY() - window.getHeight());
        window.setX(button.getX());
        window.updateElements();

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (window.isVisible()) {
                    window.hide(stage);
                } else {
                    window.show(stage);
                }
            }
        });
        button.addToStage(stage);

        UiSkinButton skinButton = new UiSkinButton("Das ist ein Text", Gdx.graphics.getWidth() - 250, 50, 250, 100);
        skinButton.addToStage(stage);

        */
    }

    ///////////////////////
    //// W I N D O W S ////
    ///////////////////////

    public void createWindows() {
        //standardWindow = new GroupWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);
        createSpaceWindow();
        createStatusbar();
    }

    public void createSpaceWindow() {
        spaceWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 800, 600, stage);
        spaceWindow.add(new UiImage(0, 0, 800, 600, "window.png"), stage);
        windowManager.addWindow(spaceWindow);
        //standardWindow.getWindowList().add(spaceWindow);
    }

    public void createStatusbar() {
        StatusBar statusBar = new StatusBar(stage, standardWindow);
        TileInfoField tileInfoField = new TileInfoField(standardWindow,stage);

        tileInfoField.StatusWindow.show(stage);

        statusBar.MainMenu.MenuContent.hide(stage);
        windowManager.addWindow(standardWindow);
    }



    public ScreenGame getGame() {
        return game;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public InputGame getInputGame() {
        return inputGame;
    }

    public void setInputGame(InputGame inputGame) {
        this.inputGame = inputGame;
    }


    public Map<String, Integer> getPlayerResources() {
        return PlayerResources;
    }

    public void setPlayerResources(Map<String, Integer> playerResources) {
        PlayerResources = playerResources;
    }

    public GroupWindow getStandardWindow() {
        return standardWindow;
    }
}
