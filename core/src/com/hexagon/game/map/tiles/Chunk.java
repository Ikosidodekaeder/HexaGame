package com.hexagon.game.map.tiles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;
import com.hexagon.game.map.Point;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.HexModelAnimated;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.util.SettingsUtil;

/**
 * Created by Sven on 16.02.2018.
 */

public class Chunk {

    public static int RANGE_X = 22;
    public static int RANGE_Z_1 = 4;
    public static int RANGE_Z_2 = 19;

    private Array<RenderTile> renderTiles = new Array<>();

    private Point start;
    private Point stop;

    public Chunk() {

    }

    public void updateBounds() {
        start = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        stop = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (int i=0; i<renderTiles.size; i++) {
            RenderTile tile = renderTiles.get(i);
            if (tile.getTileLocation().getX() < start.getX()) {
                start.setX((int) tile.getTileLocation().getX());
            }
            if (tile.getTileLocation().getX() > stop.getX()) {
                stop.setX((int) tile.getTileLocation().getX());
            }
            if (tile.getTileLocation().getY() < start.getY()) {
                start.setY((int) tile.getTileLocation().getY());
            }
            if (tile.getTileLocation().getY() > stop.getY()) {
                stop.setY((int) tile.getTileLocation().getY());
            }
        }
    }

    public boolean isInside(float x1, float y1, float x2, float y2) {
        /*System.out.println(x1 + " " + y1 + " // " + x2 + " " + y2 + "\n" +
                start.toString() + ", " + stop.toString());*/
        return (x1 <= start.getX()
                && x2 >= start.getX()
                && y1 <= start.getY()
                && y2 >= start.getY());
    }

    // TODO: Improve shadow rendering
    public void renderShadows(ModelBatch shadowBatch, Environment environment, Camera camera) {
        for (RenderTile tile : renderTiles) {
            if (tile.getTileLocation().getX() < camera.position.x - 21
                    || tile.getTileLocation().getX() > camera.position.x + 21) {
                continue;
            }
            if (tile.getTileLocation().getY() > camera.position.z + 5
                    || tile.getTileLocation().getY() < camera.position.z - 18) {
                continue;
            }
            HexModel model = tile.getModel();
            shadowBatch.render(model.getModelInstance());
            if (tile.getStructures().size() > 0) {
                for (HexModel structure : tile.getStructures()) {
                    shadowBatch.render(structure.getModelInstance(), environment);
                }
            }
        }
    }

    public void render(ModelBatch modelBatch, Environment environment, Camera camera, float delta) {
        for (RenderTile tile : renderTiles) {
            if (tile.getTileLocation().getX() < camera.position.x - RANGE_X
                    || tile.getTileLocation().getX() > camera.position.x + RANGE_X) {
                continue;
            }
            if (tile.getTileLocation().getY() > camera.position.z + RANGE_Z_1
                    || tile.getTileLocation().getY() < camera.position.z - RANGE_Z_2) {
                continue;
            }
            if (!SettingsUtil.isLowGraphics()) {
                // Only when using good graphics, update the animated models
                HexModel model = tile.getModel();
                if (model instanceof HexModelAnimated) {
                    ((HexModelAnimated) model).update(delta);
                }
            }
            /*modelBatch.render(model.getModelInstance(), environment);
            ScreenGame.renderedTiles++;
            if (tile.getStructures().size() > 0) {
                for (HexModel structure : tile.getStructures()) {
                    modelBatch.render(structure.getModelInstance(), environment);
                }
            }*/
            tile.render(modelBatch, environment);
            ScreenGame.renderedTiles++;
        }
    }

    public Array<RenderTile> getRenderTiles() {
        return renderTiles;
    }

    public Point getStart() {
        return start;
    }

    public Point getStop() {
        return stop;
    }
}
