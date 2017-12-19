package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Sven on 18.12.2017.
 */

public class UiButton extends UiElement {

    private TextButton textButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;

    public UiButton(String text, float x, float y, float width, float height) {
        super(x, y, width, height);
        font = new BitmapFont();
        skin = new Skin();

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        textButton = new TextButton(text, textButtonStyle);
        textButton.setX(x);
        textButton.setY(y);
    }

    public UiButton(String text, float x, float y, float width, float height, Stage stage, ChangeListener changeListener) {
        this(text, x, y, width, height);

        addToStage(stage);
        addListener(changeListener);
    }

    public TextButton getTextButton() {
        return textButton;
    }

    @Override
    public void addToStage(Stage stage) {
        stage.addActor(this.textButton);
    }

    @Override
    public void show(Stage stage) {
        this.textButton.setVisible(true);
    }

    @Override
    public void hide(Stage stage) {
        this.textButton.setVisible(false);
    }

    public void addListener(ChangeListener listener) {
        this.textButton.addListener(listener);
    }

    @Override
    public void setDisplayX(float x) {
        super.setDisplayX(x);
        textButton.setX(x);
    }

    @Override
    public void setDisplayY(float y) {
        super.setDisplayX(y);
        textButton.setY(y);
    }
}
