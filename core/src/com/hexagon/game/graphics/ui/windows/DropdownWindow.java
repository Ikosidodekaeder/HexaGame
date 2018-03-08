package com.hexagon.game.graphics.ui.windows;

import com.hexagon.game.graphics.ui.UiElement;

/**
 * Created by Sven on 19.12.2017.
 *
 * Orders the added ui Elements neatly
 *
 */

public class DropdownWindow extends Window {

    boolean autoSize = false;

    private int defaultColumns = 0;

    public DropdownWindow(float windowX, float windowY, float width, float height, float marginLeftRight, float marginTopBottom) {
        super(windowX, windowY, width, height); // width and height will be set later
        if (width == 0 && height == 0) {
            autoSize = true;
        }
    }

    public void orderAllNeatly() {
        if (defaultColumns == 0) {
            throw new RuntimeException("No default column amount set");
        }
        orderAllNeatly(defaultColumns);
    }

    public void orderAllNeatly(int columns) {
        defaultColumns = columns;
        float maxX = 0;
        for (int i=0; i<elementList.size(); i++) {
            UiElement element = elementList.get(i);
            int row = i / columns;
            int col = i % columns;

            if (columns > 1) {
                element.setX(col * element.getWidth() + 10);
            }
            element.setY(getHeight() - (row + 1) * element.getHeight());

            element.setDisplayX(getX() + element.getX());
            element.setDisplayY(getHeight() - getY() - element.getY() - element.getHeight());

            maxX = Math.max(maxX, element.getX());
        }
        if (autoSize) {
            UiElement firstElement = elementList.get(0);
            UiElement lastElement = elementList.get(elementList.size()-1);
            setWidth(maxX + lastElement.getWidth() + 20 - firstElement.getX());
            setHeight(lastElement.getY() + lastElement.getHeight() + 20 - firstElement.getY());
        }
    }
}
