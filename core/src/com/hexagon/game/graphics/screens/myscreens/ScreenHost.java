package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.UiImage;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.MapManager;
import com.hexagon.game.map.generator.GeneratorCallback;
import com.hexagon.game.map.generator.MapGenerator;
import com.hexagon.game.map.generator.TileGenerator;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.structures.resources.ResourceType;
import com.hexagon.game.map.structures.resources.StructureResource;
import com.hexagon.game.map.tiles.Biome;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.util.MenuUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sven on 14.12.2017.
 */

/**
 * Draws the Hexagonal Map generated by the mapgenerator
 */
public class ScreenHost extends HexagonScreen {

    private SpriteBatch batch;
    private BitmapFont font;

    public ScreenHost() {
        super(ScreenType.GENERATOR);
    }

    private List<TileGenerator> setupBiomeGenerator(final MapGenerator mapGenerator){

        // Tile Generators
        List<TileGenerator> res = new ArrayList<>();

        //biomeGenerator
        res.add(new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (x == 0 && y == 0) {
                    return tile;
                }
                Biome biomeLast;
                if (y > 0) {
                    biomeLast = mapGenerator.getGeneratedTiles()[x][y - 1].getBiome();
                } else {
                    biomeLast = mapGenerator.getGeneratedTiles()[x - 1][y].getBiome();
                }
                if (biomeLast != Biome.WATER
                        && biomeLast != Biome.ICE
                        && random.nextInt(100) < 20) {
                    tile.setBiome(biomeLast);
                } else {
                    int r = random.nextInt(2);
                    if (r == 0) {
                        tile.setBiome(Biome.DESERT);
                    } else if (r == 1) {
                        tile.setBiome(Biome.PLAINS);
                    }
                }
                return tile;
            }
        });

        // Add the ice generator last! Highest Priority == called last
        //iceGenerator
        res.add( new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (x < 5 || x > mapGenerator.getSizeX() - 5) {
                    if (x <= 2 || x >= mapGenerator.getSizeX() - 2) {
                        tile.setBiome(Biome.ICE);
                    } else if (tile.getBiome() != Biome.ICE){
                        tile.setBiome(Biome.WATER);
                    }
                }
                if (y < 5 || y > mapGenerator.getSizeY() - 5) {
                    if (y <= 2 || y >= mapGenerator.getSizeY() - 2) {
                        tile.setBiome(Biome.ICE);
                    } else if (tile.getBiome() != Biome.ICE){
                        tile.setBiome(Biome.WATER);
                    }
                }

                return tile;
            }
        });

        //resourceGenerator
        res.add( new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (tile.getBiome() != Biome.PLAINS) {
                    return tile;
                }
                if (tile.getStructure() != null) {
                    return tile;
                }
                if (random.nextInt(100) <= 20) {
                    int r = random.nextInt(ResourceType.values().length - 1);
                    ResourceType resourceType = ResourceType.values()[r];
                    tile.setStructure(new StructureResource(resourceType));
                }
                return tile;
            }
        });

        //treeGenerator
        res.add( new TileGenerator() {
            @Override
            public Tile generate(Tile tile, int x, int y, Random random) {
                if (tile.getBiome() != Biome.PLAINS) {
                    // Trees can only spawn on grassland
                    return tile;
                }
                if (tile.getStructure() != null) {
                    return tile;
                }
                if (random.nextInt(100) <= 80) {
                    tile.setStructure(new Structure(StructureType.FOREST));
                }
                return tile;
            }
        });

        return res;
    }

    private void setupUserInterface(){

        UiButton button = new UiButton("Host a session", 50, Gdx.graphics.getHeight() - 50, 100, 50);

        button.addToStage(stage);


        //MenuUtil.getInstance().createStandardMenu(this);
        final GroupWindow standardWindow = new GroupWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);

        FadeWindow fadeWindow = new FadeWindow(MenuUtil.getInstance().getX(), MenuUtil.getInstance().getY(), 224, 600, stage);
        fadeWindow.show(stage);
        fadeWindow.add(new UiImage(0, 0, 224, 600, "sidebar.png"), stage);



        standardWindow.getWindowList().add(fadeWindow);

        /*
         * Subwindow 1: Players
         */
        final FadeWindow subwindowPlay = new FadeWindow(fadeWindow.getX() + fadeWindow.getWidth() + 10, fadeWindow.getY(), 800 - fadeWindow.getWidth(), 600, stage);
        subwindowPlay.add(new UiImage(0, 0, 558, 600, "window_small.png"), stage);

        UiButton playText = new UiButton("Players", 40, subwindowPlay.getHeight() - 60, 100, 40);
        UiButton playGenerate = new UiButton("Generate World", 40, subwindowPlay.getHeight() - 100, 200, 40);

        playGenerate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.GENERATOR);
            }
        });

        subwindowPlay.add(playText, stage);
        subwindowPlay.add(playGenerate, stage);

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

        this.windowManager.getWindowList().add(standardWindow);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        GameManager gameManager = new GameManager();


        setupUserInterface();

    }

    @Override
    public void show() {
        super.show();

        System.out.println("Showing generator");

        /*
         * Start creating the world
         */

        final MapGenerator mapGenerator = new MapGenerator(100, 40, 2);
        List<TileGenerator> Biomes = setupBiomeGenerator(mapGenerator);
        for(TileGenerator generator : Biomes)
            mapGenerator.getTileGeneratorList().add(generator);

        mapGenerator.setCallback(new GeneratorCallback() {
            @Override
            public void generatorFinished() {

                final HexMap hexMap = new HexMap(mapGenerator.getSizeX(), mapGenerator.getSizeY());
                hexMap.setTiles(mapGenerator.getGeneratedTiles());

                MapManager.getInstance().setCurrentHexMap(hexMap);

                ScreenManager.getInstance().setCurrentScreen(ScreenType.GAME);
            }
        });

        mapGenerator.startGenerating();
    }

    @Override
    public void render(float delta) {
        ScreenManager.getInstance().clearScreen(0.2f, 0.25f, 0.35f);
        batch.begin();
        font.draw(batch, "Generator", 20, 20);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        stage.dispose();
        batch.dispose();
    }
}