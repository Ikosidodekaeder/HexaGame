package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.util.FontManager;
import com.hexagon.game.util.Nullable;

/**
 * Created by Johannes on 16.02.2018.
 */

public class UILabel extends UiElement {
    public com.badlogic.gdx.scenes.scene2d.ui.Label getLabel() {
        return Label;
    }

    private Label       Label;
    private LabelStyle  style;
    private GlyphLayout glyphLayout;
    private Color       background;

    public UILabel(float x, float y, float width, float height, int fontsize, String text) {
        this(x, y, width, height, fontsize, text, Color.WHITE, null);
    }

    public UILabel(float x, float y, float width, float height, int fontsize, String text, Color color, @Nullable Color background) {
        super(x,y,width,height);

        //long start = System.currentTimeMillis();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(FontManager.handlePiximisa);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontsize;
        BitmapFont font32 = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        style = new LabelStyle();
        style.font = font32;
        style.fontColor = color;

        if (height <= 0) {
            glyphLayout = new GlyphLayout();
            glyphLayout.setText(font32, text);
            //setHeight(font32.getLineHeight());
            setHeight(glyphLayout.height);
        }
        if (width <= 0) {
            glyphLayout = new GlyphLayout();
            glyphLayout.setText(font32, text);
            setWidth(glyphLayout.width);
        }
        this.background = background;

        Label = new Label(text, style);
        Label.setX(x);
        Label.setY(y);
    }

    public UILabel( float x, float y, float width, float height,int fontsize, Stage stage,String text, ChangeListener changeListener) {
        this(x, y, width, height, fontsize, text);

        addToStage(stage);
        addListener(changeListener);
    }

    @Override
    public void render(ShapeRenderer renderer) {
        if (background == null) {
            return;
        }
        renderer.setColor(background);
        renderer.rect(getLabel().getX()-2, getLabel().getY()+2, width+8, height+4);
    }


    @Override
    public void addToStage(Stage stage) {
        stage.addActor(this.Label);
    }

    @Override
    public void removeFromStage(Stage stage) {
        stage.getActors().removeValue(this.Label, false);
    }

    @Override
    public void show(Stage stage) {
        this.Label.setVisible(true);
    }

    @Override
    public void hide(Stage stage) {
        this.Label.setVisible(false);
    }

    public void addListener(ChangeListener listener) {
        this.Label.addListener(listener);
    }

    @Override
    public void setDisplayX(float x) {
        super.setDisplayX(x);
        Label.setX(x);
    }

    @Override
    public void setDisplayY(float y) {
        super.setDisplayY(y);
        Label.setY(y);
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBackground() {
        return background;
    }
}
