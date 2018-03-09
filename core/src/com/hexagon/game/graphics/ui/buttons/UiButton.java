package com.hexagon.game.graphics.ui.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.util.FontManager;

/**
 * Created by Sven on 18.12.2017.
 */

public class UiButton extends UiElement {

    private TextButton      textButton;
    private GlyphLayout     glyphLayout;
    private BitmapFont      font32;
    private TextButton.TextButtonStyle style;

    private EnterEvent      enterEvent;
    private ExitEvent       exitEvent;
    private ChangeListener  clickEvent;

    public UiButton(final String text, float x, float y, float width, float height, int fontSize) {
        super(x, y, width, height);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FontManager.handlePiximisa);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        font32 = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        style = new TextButton.TextButtonStyle();
        style.font = font32;
        style.fontColor = Color.WHITE;
        style.overFontColor = Color.GRAY;
        style.downFontColor = Color.DARK_GRAY;

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

        textButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (enterEvent != null) {
                    enterEvent.enter(UiButton.this);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (exitEvent != null) {
                    exitEvent.exit(UiButton.this);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (clickEvent != null) {
                    clickEvent.changed(null, event.getTarget());
                    return true;
                }
                return false;
            }
        });

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
        //this.textButton.addListener(listener);
        this.clickEvent = listener;
    }

    @Override
    public void setDisplayX(float x) {
        super.setDisplayX(x);
        textButton.setX(x);
    }

    @Override
    public void setDisplayY(float y) {
        super.setDisplayY(y);
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

    public EnterEvent getEnterEvent() {
        return enterEvent;
    }

    public void setEnterEvent(EnterEvent enterEvent) {
        this.enterEvent = enterEvent;
    }

    public ExitEvent getExitEvent() {
        return exitEvent;
    }

    public void setExitEvent(ExitEvent exitEvent) {
        this.exitEvent = exitEvent;
    }

    public abstract static class EnterEvent {

        public abstract void enter(UiButton button);

    }

    public abstract class ExitEvent {

        public abstract void exit(UiButton button);

    }
}
