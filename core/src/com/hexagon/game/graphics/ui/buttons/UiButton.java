package com.hexagon.game.graphics.ui.buttons;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.util.ConsoleColours;
import com.hexagon.game.util.FontManager;

/**
 * Created by Sven on 18.12.2017.
 */

public class UiButton extends UiElement {

    private TextButton textButton;
    private TextButton.TextButtonStyle style;
    private GlyphLayout glyphLayout;
    private BitmapFont font32;

    public UiButton(String text, float x, float y, float width, float height, int fontSize) {
        super(x, y, width, height);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FontManager.handlePiximisa);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        font32 = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        style = new TextButton.TextButtonStyle();
        style.font = font32;

        if (height <= 0) {
            setHeight(font32.getLineHeight());
        }
        glyphLayout = new GlyphLayout();
        if (width <= 0) {
            glyphLayout.setText(font32, text);
            setWidth(glyphLayout.width);
        }

        textButton = new TextButton(text, style);
        textButton.setX(x);
        textButton.setY(y);

    }

    public UiButton(String text, float x, float y, float width, float height) {
        this(text, x, y, width, height, 32);
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
    public void removeFromStage(Stage stage) {
        stage.getActors().removeValue(this.textButton, false);
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

    public float getFontHeight() {
        return textButton.getStyle().font.getLineHeight();
    }

    public void setText(String text) {
        getTextButton().setText(text);
        glyphLayout.setText(font32, text);
        setWidth(glyphLayout.width);
    }
}
