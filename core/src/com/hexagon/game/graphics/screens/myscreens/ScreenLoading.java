package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.hexagon.game.graphics.ModelManager;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.menu.ScreenMenuSuper;
import com.hexagon.game.input.InputManager;

/**
 * Created by Sven on 14.12.2017.
 */

public class ScreenLoading extends HexagonScreen {

    public static float loadedIndividual = 0;

    private SpriteBatch batch;
    private BitmapFont font;
    private float loaded = 0;
    private String currentlyLoading = "";

    private float brightness = -1;
    private boolean renderedOnce = false;

    public ScreenLoading() {
        super(ScreenType.LOADING);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        font = new BitmapFont();


        // Create Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Piximisa.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        font = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        // Load Models
        new ModelManager().loadAllModels();

        ScreenManager.getInstance().screenMenuSuper = new ScreenMenuSuper();

        // Start loading thread
        final Thread loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //noinspection StatementWithEmptyBody
                while (!renderedOnce) {
                    // wait
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int i = 0;
                for (final HexagonScreen screen : ScreenManager.getInstance().getScreenList()) {
                    i++;
                    if (screen.getScreenType() == ScreenType.LOADING) {
                        continue;
                    }
                    loadedIndividual = 0;
                    currentlyLoading = screen.getScreenType().name();

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            screen.create();

                            render(0.01f);
                        }
                    });
                    loadedIndividual = 1;
                    loaded = ((float) i) / ScreenManager.getInstance().getScreenList().size();
                    if (i == ScreenManager.getInstance().getScreenList().size() - 1) {
                        // done loading
                        // run this on the render thread to prevent two threads accessing the same list at the same time
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                doneLoading();
                            }
                        });
                    }
                }
            }
        });
        loadThread.start();


        ScreenManager.getInstance().setCurrentScreen(ScreenType.LOADING);
    }

    @Override
    public void show() {
        renderedOnce = false;
    }

    public void doneLoading() {
        InputManager.getInstance().register(this.getStage());
        ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
    }

    @Override
    public void render(float delta) {
        brightness = 1;
        /*if (brightness <= 1) {
            if (brightness < 0) {
                brightness = 0;
            } else {
                brightness += 0.45 * delta;
            }
        }*/

        ScreenManager.getInstance().clearScreen(0.05f * brightness, 0.15f * brightness, 0.2f * brightness);
        batch.begin();
        if (!renderedOnce) {
            font.draw(batch, "Loading", 50, 100);
        } else {
            font.draw(batch, "Loading " + (Math.round(loaded * 100)) + "% (" + currentlyLoading + " " + (Math.round(loadedIndividual * 100)) + "%)", 50, 100);
        }
        batch.end();
        renderedOnce = true;
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
        batch.dispose();
    }
}
