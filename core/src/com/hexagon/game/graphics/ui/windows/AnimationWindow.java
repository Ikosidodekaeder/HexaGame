package com.hexagon.game.graphics.ui.windows;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Sven on 20.12.2017.
 */

public class AnimationWindow extends DropdownWindow {

    private static final float SPEED = 5f;

    private float pos = 0; // position in percent between the start and stop vector
    private float x1, y1, x2, y2;
    private Stage stage;

    private boolean retracting = false;

    /**
     *
     * @param x the "invisible" x position
     * @param y the "invisible" y position
     * @param x2 the x position the window moves to when setting visible to true
     * @param y2 the y position the window moves to when setting visible to true
     * @param width
     * @param height
     * @param stage
     */
    public AnimationWindow(float x, float y, float x2, float y2, float width, float height, Stage stage) {
        super(x, y, width, height, 5, 5);

        this.stage = stage;
        this.x1 = x;
        this.y1 = y;
        this.x2 = x2;
        this.y2 = y2;

        retracting = false;
    }

    public void toggleVisibility() {
        toggleVisibility(null);
    }

    public void toggleVisibility(InputProcessor parent) {
        if (isVisible()) {
            retracting = true;
        } else {
            show(stage);
        }
    }

    private float function(float x) {
        return (float) (-0.15f*(Math.pow(x-0.5f, 2))+0.042f);
    }

    /*private float function(float x) {
        return (float) (-0.09f*(Math.pow(x, 2))+0.1);
    }*/

    @Override
    public void render(ShapeRenderer renderer) {
        super.render(renderer);

        if (isVisible() && !retracting) {
            if (pos >= 1.0f) {
                return;
            }

            pos += function(pos);

            if (pos > 1.0f) {
                pos = 1;
            }
        } else {
            if (pos <= 0.0f) {
                return;
            }

            pos -= function(pos);

            if (pos < 0.0f) {
                pos = 0;
                super.hide(stage);
                retracting = false;
            }
        }

        x = x1 * (1 - pos) + x2 * pos;
        y = y1 * (1 - pos) + y2 * pos;
        super.orderAllNeatly();
        super.updateElements();
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);
        retracting = false;
    }

    @Override
    public void hide(Stage stage) {
        retracting = true;
    }
}
