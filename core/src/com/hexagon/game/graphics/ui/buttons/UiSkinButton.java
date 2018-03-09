package com.hexagon.game.graphics.ui.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.util.FontManager;

/**
 * Created by Sven on 18.12.2017.
 */

public class UiSkinButton extends UiElement {

    private TextButton textButton;
    private TextButton.TextButtonStyle style;
    private Skin skin;

    public UiSkinButton(String text, float x, float y, float width, float height) {
        super(x, y, width, height);
        skin = new Skin();
        skin.add("up", new Texture(Gdx.files.internal("images/arrow.png")));
        //skin.add("down", );


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FontManager.handlePiximisa);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        BitmapFont font32 = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        style = new TextButton.TextButtonStyle();
        style.font = font32;

        style.up = skin.getDrawable("up"); // image for when not pressed
        //style.down = skin.getDrawable("down"); // image when pressed

        //style.up = skin.getDrawable(drawablename); // image for when not pressed
        //style.down = skin.newDrawable(drawablename, new Color(1f,1f,1f,0.5f)); // image when pressed

        textButton = new TextButton(text, style);
        textButton.setX(x);
        textButton.setY(y);
    }

    public UiSkinButton(String text, float x, float y, float width, float height, Stage stage, ChangeListener changeListener) {
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
}
