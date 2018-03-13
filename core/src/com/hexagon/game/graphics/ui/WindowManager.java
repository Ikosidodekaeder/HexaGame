package com.hexagon.game.graphics.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.windows.DropdownScrollableWindow;
import com.hexagon.game.graphics.ui.windows.Window;
import com.hexagon.game.graphics.ui.windows.WindowNotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sven on 19.12.2017.
 */

public class WindowManager implements InputProcessor {

    private List<Window> windowList;

    public WindowManager() {
        windowList = new ArrayList<>();
    }


    public void addWindow(Window window) {
        windowList.add(window);
        System.out.println("Added window " + windowList.size());
        sortAll();
    }

    public List<Window> getWindowList() {
        return windowList;
    }

    public void render(ShapeRenderer renderer) {
        for (Window window : windowList) {
            window.render(renderer);
        }
    }

    public void removeNotifications(Stage stage) {
        List<Window> toRemove = new ArrayList<>();
        for (Window window : windowList) {
            if (window instanceof WindowNotification) {
                toRemove.add(window);
            }
        }
        for (Window window : toRemove) {
            window.removeAll(stage);
            windowList.remove(window);
        }
    }

    public void removeAll(Stage stage) {
        System.out.println("WindowList size: " + windowList.size());
        for (Window window : windowList) {
            window.removeAll(stage);
        }
        windowList.clear();
    }

    public void remove(Stage stage, Window window) {
        window.removeAll(stage);
        windowList.remove(window);
    }

    public void sortAll() {
        Collections.sort(windowList);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    private int startTouchY = -1;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        startTouchY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    int lastDiff = 0;

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int diff = screenY - startTouchY;
        int amount = 0;
        if (diff > 10) {
            amount = -1;
            startTouchY = screenY;
        } else if (diff < -10) {
            amount = 1;
            startTouchY = screenY;
        }
        if (lastDiff < 0 && diff >= 0) {
            startTouchY = screenY;
        } else if (lastDiff > 0 && diff <= 0) {
            startTouchY = screenY;
        }
        lastDiff = diff;
        for (Window window : windowList) {
            if (!window.isVisible()) {
                continue;
            }
            if (window instanceof DropdownScrollableWindow) {
                if (((DropdownScrollableWindow) window).scroll(amount)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        for (Window window : windowList) {
            if (!window.isVisible()) {
                continue;
            }
            if (window instanceof DropdownScrollableWindow) {
                if (((DropdownScrollableWindow) window).scroll(amount)) {
                    return true;
                }
            }
        }
        return false;
    }
}
