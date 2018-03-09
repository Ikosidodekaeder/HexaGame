package com.hexagon.game.graphics.ui.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;

/**
 * Created by Sven on 25.02.2018.
 */

public class WindowNotification extends Window {

    private static final int SIZEX = 700;
    private static final int SIZEY = 300;

    public WindowNotification(String text, final Stage stage, final WindowManager windowManager) {
        super(Gdx.graphics.getWidth()/2 - SIZEX/2,
                Gdx.graphics.getHeight()/2 - SIZEY/2,
                SIZEX, SIZEY);
        super.setPriority(100);


        add(new UILabel(getX() - SIZEX/2, getY() - SIZEY/2 + 50, SIZEX, 100, 32, text), stage);
        UiButton button = new UiButton("[Confirm]", getX() - SIZEX/2 + 20, getY() - getHeight() + 50, 100, 50);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                windowManager.remove(stage, WindowNotification.this);
            }
        });
        add(button, stage);
        show(stage);
        windowManager.addWindow(this);
    }
}
