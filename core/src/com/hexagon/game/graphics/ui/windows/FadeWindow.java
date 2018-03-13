package com.hexagon.game.graphics.ui.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.graphics.ui.UiImage;

/**
 * Created by Sven on 20.12.2017.
 */

public class FadeWindow extends Window {

    private static final float SPEED = 5f;

    private float alpha = 0;
    private Stage stage;

    public FadeWindow(float x, float y, float width, float height, Stage stage) {
        super(x, y, width, height);

        this.stage = stage;
    }

    public void toggleVisibility() {
        toggleVisibility(null);
    }

    public void toggleVisibility(InputProcessor parent) {
        if (isVisible()) {
            setVisible(false);
            if (parent != null) {

            }
        } else {
            show(stage);
            if (parent != null) {

            }
        }
    }

    @Override
    public void render(ShapeRenderer renderer) {
        if (isVisible()) {
            if (alpha >= 1) {
                return;
            }
            float toAdd = SPEED * Gdx.graphics.getDeltaTime();
            alpha += toAdd;
            if (alpha > 0.75f) {
                alpha = 0.75f;
            }
            for (UiElement element : getElementList()) {
                if (element instanceof UiImage) {
                    UiImage image = (UiImage) element;
                    Color color = image.getImage().getColor();
                    color.set(color.r, color.g, color.b, alpha);
                }
            }
        } else {
            if (alpha <= 0) {
                return;
            }
            float toRemove = SPEED * Gdx.graphics.getDeltaTime();
            alpha -= toRemove;
            if (alpha < 0.0f) {
                alpha = 0;
                super.hide(stage);
            }
            for (UiElement element : getElementList()) {
                if (element instanceof UiImage) {
                    UiImage image = (UiImage) element;
                    Color color = image.getImage().getColor();
                    color.set(color.r, color.g, color.b, alpha);
                }
            }
        }
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);
    }

    @Override
    public void hide(Stage stage) {
        setVisible(false);
    }
}
