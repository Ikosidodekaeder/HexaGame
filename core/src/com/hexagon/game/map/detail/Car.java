package com.hexagon.game.map.detail;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.util.AStar;
import com.hexagon.game.util.HexVector;
import com.hexagon.game.util.HexagonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sven on 14.02.2018.
 */

public class Car {

    private static int uid = 0;

    private List<Car>   collisionCars = new ArrayList<>();
    private Tile        lastTile;

    private double MAX_SPEED = 2f;

    private int                     i = 0;
    private List<CarTile>           path = new ArrayList<>();
    private ModelInstance           instance;
    private TileLocation            currentLocation;
    private HexVector               currentVector;
    private HexVector               direction;

    private double                  velocity = 0;

    private int                     id = -1;

    private final Vector3 tmp = new Vector3();

    private boolean forward = true;

    private float wait = 0.0f;

    public Car(List<AStar.ATile> aTiles, float wait) {
        id = nextId();

        this.wait = wait;

        Model boxModel2 = new ModelBuilder().createBox(0.15f, 0.15f, 0.15f,
                new Material(ColorAttribute.createDiffuse(0.7f, 0.3f, 0.3f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        ScreenGame game = (ScreenGame) ScreenManager.getInstance().getCurrentScreen();

        HexVector before = null;
        for (AStar.ATile aTile : aTiles) {
            TileLocation location = HexagonUtil.getTileLocation(aTile.getX(), aTile.getY());
            HexVector vector = new HexVector(location.getX(), location.getY());

            Tile tile = game.getCurrentMap().getTileAt(aTile.getX(), aTile.getY());

            if (before != null) {
                HexVector diff = vector.copy().subtract(before).normalize();

                // Create street models along the path
                //Tile tile = game.getCurrentMap().getTileAt(aTile.getX(), aTile.getY());
                tile.setStructure(new Structure(StructureType.STREET));
                RenderTile renderTile = tile.getRenderTile();
                if (renderTile == null) {
                    throw new RuntimeException("RenderTile is null");
                }
                if (renderTile.getStructures() != null) {
                    renderTile.getStructures().clear();
                } else {
                    renderTile.setStructures(new ArrayList<HexModel>());
                }
                HexModel street = new HexModel(new ModelInstance(game.getStreetModel()));
                street.move((float) location.getX(), 0.01f, (float) location.getY());
                renderTile.getStructures().add(street);


                HexVector right = diff.copy().rotate270().multiply(0.2).add(vector);

                ModelInstance box = new ModelInstance(boxModel2);
                box.transform.translate((float) right.getX(), 0, (float) right.getY());
                game.getDebugModels().add(box);

                path.add(
                    new CarTile(
                        new TileLocation(right.getX(), right.getY()),
                        tile
                    )
                );
                //path.add(tile);
                //path.add(new TileLocation(right.getX(), right.getY()));
            } else {
                //path.add(location);
                path.add(
                        new CarTile(location, tile)
                );
            }

            before = vector;
        }


        Collections.reverse(aTiles);

        boxModel2 = new ModelBuilder().createBox(0.15f, 0.15f, 0.15f,
                new Material(ColorAttribute.createDiffuse(0.3f, 0.7f, 0.3f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        before = null;
        for (AStar.ATile aTile : aTiles) {
            TileLocation location = HexagonUtil.getTileLocation(aTile.getX(), aTile.getY());
            HexVector vector = new HexVector(location.getX(), location.getY());

            Tile tile = game.getCurrentMap().getTileAt(aTile.getX(), aTile.getY());
            if (before != null) {
                HexVector diff = vector.copy().subtract(before).normalize();
                HexVector left = diff.copy().rotate270().multiply(0.2).add(vector);

                ModelInstance box = new ModelInstance(boxModel2);
                box.transform.translate((float) left.getX(), 0, (float) left.getY());
                game.getDebugModels().add(box);
                //path.add(new TileLocation(left.getX(), left.getY()));
                path.add(
                        new CarTile(
                                new TileLocation(left.getX(), left.getY()),
                                tile
                        )
                );
            } else {
                path.add(
                        new CarTile(location, tile)
                );
            }



            before = vector;
        }



        /*Model boxModel = new ModelBuilder().createBox(0.2f, 0.2f, 0.2f,
                new Material(ColorAttribute.createDiffuse(0.3f, 0.3f, 0.6f, 1)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);*/

        currentLocation     = path.get(0).location.copy();
        currentVector       = new HexVector(0, 0);
        direction           = new HexVector(0, 0);
        //instance = new ModelInstance(boxModel);
        instance = new ModelInstance(game.getTruckModel());
        //instance.transform.setToTranslationAndScaling(, 0.06f, 0.06f, 0.06f);
        Matrix4 m = instance.transform.translate((float) currentLocation.getX(), 0, (float) currentLocation.getY());

        m = m.scale(0.06f, 0.06f, 0.06f);
        instance.transform.set(m);

        //MAX_SPEED = Math.random() + 1.0;

    }

    private float ignoreCollision = 0;

    public void update(float delta) {
        if (wait > 0) {
            wait -= delta;
            return;
        }

        Tile tile = path.get(i).tile;
        TileLocation destination = path.get(i).location;

        CollisionType collided = CollisionType.NONE;
        if (ignoreCollision > 0) {
            ignoreCollision -= delta;
        }
        if (ignoreCollision < 1.0f) {
            collided = isCollided(tile);
            if (collided != CollisionType.STOP && i < path.size() - 1) {
                collided = isCollided(path.get(i + 1).tile);
            }
            if (collided != CollisionType.STOP) {
                ignoreCollision += delta + 0.01;
            }
        }

        currentVector.setX(destination.getX() - currentLocation.getX());
        currentVector.setY(destination.getY() - currentLocation.getY());
        //if (currentVector.getX() > 0.0 || currentVector.getY() > 0.0) {
            currentVector.normalize();
            direction.setX(currentVector.getX());
            direction.setY(currentVector.getY());


            float angle = (float) Math.toDegrees(Math.atan2(currentVector.getY(), currentVector.getX()));

            if (collided == CollisionType.STOP) {
                if (velocity > 0) {
                    velocity *= 0.2;
                }
            } else if (collided == CollisionType.CLOSE) {
                if (velocity > MAX_SPEED*0.2) {
                    velocity *= 0.7;
                }
            } else {
                if (velocity <= MAX_SPEED) {
                    velocity *= 1.01;
                    velocity += 0.01;
                    if (velocity > MAX_SPEED) {
                        velocity = MAX_SPEED;
                    }
                }
            }

            currentVector.multiply(delta * velocity);

            System.out.println(currentVector.toString());

            currentLocation.setX(currentLocation.getX() + currentVector.getX());
            currentLocation.setY(currentLocation.getY() + currentVector.getY());

            //System.out.println("Going to " + destination.getX() + ", " + destination.getY() + " /// " + currentLocation.getX() + ", " + currentLocation.getY());

            /*instance.transform
                    .translate((float) currentLocation.getX(), 0, (float) currentLocation.getY())
                    .setToRotation(0, 1.0f, 0, angle);*/
            /*Matrix4 m = instance.transform;
            m = m//.setToRotation(0, 1.0f, 0, angle)
                    .translate((float) (deltaX), 0, (float) (deltaY));
            instance.transform.set(m);*/
            //instance.transform.rotate(0, 1.0f, 0, angle);
            //instance.transform.setToRotation(0, 1.0f, 0, angle);

            //instance.transform.setToTranslation()
                //.rotate(0, 1.0f, 0, angle);//, 0.06f, 0.06f, 0.06f);
            //instance.transform.setToRotation(0, 1.0f, 0, angle);
            //instance.transform.setToRotation(0, 1.0f, 0, angle + 90)
            //instance.transform.idt();
            //instance.transform.setToRotation(0, 1, 0, angle);
            //instance.transform.scale(0.06f, 0.06f, 0.06f);
            instance.transform.setToScaling(0.06f, 0.06f, 0.06f);
            instance.transform.rotate(0, 1, 0, -angle - 90);

            tmp.set((float) currentLocation.getX(), 0.08f, (float) currentLocation.getY());
            //instance.transform.scl(0.06f, 0.06f, 0.06f);
            //instance.transform.setToRotation(0, 1.0f, 0, angle + 90);
            //instance.transform.trn((float) currentLocation.getX(), 0.5f, (float) currentLocation.getY());
            instance.transform.setTranslation(tmp);

        //}

        if (currentLocation.distanceSquared(destination) <= 0.1 * 0.1) {
            /*if (forward) {
                i++;
                if (i >= path.size()) {
                    forward = false;
                    i = path.size()-1;
                }
            } else {
                i--;
                if (i < 0) {
                    forward = true;
                    i = 0;
                }
            }*/
            i++;
            if (i >= path.size()) {
                i = 0;
                velocity = 0;
            }

            lastTile = tile;
            Tile nextTile = path.get(i).tile;
            tile.getCars().remove(this);
            nextTile.getCars().add(this);
        }
    }

    private final TileLocation tmpLoc = new TileLocation(0, 0);
    private final TileLocation tmpLoc2 = new TileLocation(0, 0);
    public CollisionType isCollided(Tile tile) {

        tmpLoc.setX(this.currentLocation.getX() + direction.getX()*0.18);
        tmpLoc.setY(this.currentLocation.getY() + direction.getY()*0.18);
        tmpLoc2.setX(this.currentLocation.getX() + direction.getX()*0.35);
        tmpLoc2.setY(this.currentLocation.getY() + direction.getY()*0.35);

        collisionCars.clear();
        collisionCars.addAll(tile.getCars());
        if (lastTile != null) {
            collisionCars.addAll(lastTile.getCars());
        }
        for (int i=0; i<collisionCars.size(); i++) {
            Car otherCar = collisionCars.get(i);
            if (otherCar.equals(this)) {
                continue;
            }
            double distance = otherCar.getCurrentLocation().distanceSquared(tmpLoc);
            if (distance <= 0.12*0.12) {
                /*HexVector vector = new HexVector(
                        otherCar.getCurrentLocation().getX() - this.currentLocation.getX(),
                        otherCar.getCurrentLocation().getY() - this.currentLocation.getY()
                );*/
                // Only collide with cars that come from the right
                //if (vector.getX() > 0.1) {
                //if (this.id > otherCar.getId()) {
                    return CollisionType.STOP;
                //}
                //ignoreCollision = 2.0f;
                //}
            } else {
                double distance2 = otherCar.getCurrentLocation().distanceSquared(tmpLoc2);
                if (distance2 <= 0.22*0.22) {
                    return CollisionType.CLOSE;
                }
            }
        }
        return CollisionType.NONE;
    }

    public ModelInstance getInstance() {
        return instance;
    }

    public TileLocation getCurrentLocation() {
        return currentLocation;
    }

    public int getId() {
        return id;
    }

    private static int nextId() {
        return uid++;
    }

    private enum CollisionType {
        STOP,
        CLOSE,
        NONE;
    }

}
