package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.Components.HexaComponentPosition;
import com.hexagon.game.Logic.Components.HexaComponentTest;
import com.hexagon.game.graphics.ui.windows.FadeWindow;
import com.hexagon.game.input.HexInput;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketJoin;
import com.hexagon.game.network.packets.PacketRegister;
import com.hexagon.game.util.CameraHelper;
import com.hexagon.game.util.HexagonUtil;

import java.util.ArrayList;
import java.util.UUID;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentProducer;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.world.Engine;

/**
 * Created by Sven on 20.12.2017.
 */

public class InputGame extends HexInput {

    private static float    SPEED = 5;

    private CameraHelper    cameraHelper;

    private float           velX = 0;
    private float           velY = 0;
    private float           zoom = 0;

    private int             downX = 0;
    private int             downY = 0;

    /*private HexModel        selected;
    private Material        selectionMaterial;
    private Array<Material> originalMaterial;*/

    private Vector3         cameraLockOnTile = null;

    private ScreenGame      screenGame;

    public InputGame(ScreenGame screenGame) {
        this.screenGame     = screenGame;
        this.cameraHelper   = new CameraHelper(screenGame.getCamera());

        //selectionMaterial   = new Material();
        //selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) {
            velX = -SPEED;
        } else if (keycode == Input.Keys.D) {
            velX = SPEED;
        }
        if (keycode == Input.Keys.W) {
            velY = -SPEED;
        } else if (keycode == Input.Keys.S) {
            velY = SPEED;
        }

        if (keycode == Input.Keys.SPACE) {
            /*if (cameraLockOnTile == null) {
                cameraLockOnTile = screenGame.getCamera().position.cpy();
                Vector3 pos = cameraHelper.getCamera().position;
                cameraHelper.moveTo(new Vector3(pos.x, 2, pos.z - 2), false);
            } else {
                cameraHelper.moveTo(cameraLockOnTile, true);
                cameraLockOnTile = null;
            }*/
            FadeWindow window = screenGame.gameManager.spaceWindow;
            window.toggleVisibility(this);
            setDisableMouse(!isDisableMouse());

        }
        if (keycode == Input.Keys.H) {
            screenGame.gameManager.connect(true);
        }
        if (keycode == Input.Keys.C) {
            screenGame.gameManager.connect(false);
        }
        if (keycode == Input.Keys.ENTER) {
            if (screenGame.gameManager.server.isHost()) {
                screenGame.gameManager.server.send(new PacketRegister(
                        HexaServer.senderId, // This is the host id
                        "Raum 8"));
            }
        }
        if (keycode == Input.Keys.P) {
            if (!screenGame.gameManager.server.isHost()) {
                screenGame.gameManager.server.send(new PacketJoin(
                        HexaServer.senderId, // The sender (us)
                        "Sven",
                        UUID.fromString("e84223f7-f8dd-4ea4-8494-25ef9d27a1a9"), // The host WE want to connect to
                        "1.0"));
            }
        }
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        
        HexModel model = getObject(screenX, screenY);
        if (model != null) {
            select(model);
        }
        downX = screenX;
        downY = screenY;

        System.out.println("MÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖP\n");

        //TODO: This needs to be multiplexed to the right System for the correct player!!!!!!!!!
        Engine.getInstance().BroadcastMessage(new NotificationNewEntity(
                Engine.getInstance().getEntityManager().createID(
                        new ComponentProducer(),
                        new ComponentResource(
                                0.2f,
                                1,
                                1,
                                /*Create Signature for the requested resources produced by this system
                                * But this inline Arraylist init is bit weird, nonetheless it makes
                                * code more readable :D this is always an plus
                                */
                                new ArrayList<Component>() {{
                                    add(new HexaComponentOwner("PLAYER"));
                                    add(new HexaComponentPosition(new Point(downX,downY,0)));
                                    add(new HexaComponentTest());
                        }}
                        )
                )
        ));

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        
        float zoom = this.screenGame.getCamera().fieldOfView * 0.01f;
        if (zoom > 1.0f) {
            zoom = 1;
        }
        velX = (downX - screenX) * zoom;
        velY = (downY - screenY) * zoom;
        downX = screenX;
        downY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        
        updateSelected(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        
        zoom = amount*0.4f;
        return true;
    }

    public void update(float delta) {
        if (cameraHelper.isLocked()) {
            cameraHelper.update();
            velX = 0;
            velY = 0;
            zoom = 0;
            return;
        }
        
        if (zoom != 0) {
            zoom *= 0.8f;
            /*float fov = screenGame.getCamera().fieldOfView + zoom;
            if (fov < 20) {
                fov = 20;
            } else if (fov > 100) {
                fov = 100;
            }

            screenGame.getCamera().fieldOfView = fov;*/
            float y = screenGame.getCamera().position.y + zoom;
            if (y < 4.0f) {
                y = 4.0f;
            } else if (y > 10.0f) {
                y = 10.0f;
            } else {
                screenGame.getCamera().position.z += zoom;
            }
            screenGame.getCamera().position.y = y;

            screenGame.getCamera().update();
            if (zoom >= -0.005f && zoom <= 0.005f) {
                zoom = 0;
            }
            updateSelected(Gdx.input.getX(), Gdx.input.getY());
        }
        if (velX == 0 && velY == 0) {
            return;
        }
        updateSelected(Gdx.input.getX(), Gdx.input.getY());
        Camera camera = screenGame.getCamera();
        camera.position.add(velX * delta, 0, velY * delta);

        camera.update();

        if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY *= 0.87;
            if (velY >= -0.01 && velY <= 0.01) {
                velY = 0;
            }
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX *= 0.87;
            if (velX >= -0.01 && velX <= 0.01) {
                velX = 0;
            }
        }
    }

    private Vector3 position = new Vector3();

    public void updateSelected(int x, int y) {
        HexModel model = getObject(x, y);
        if (model != null) {
            select(model);
        }
    }

    public HexModel getObject(float x, float y) {
        final Ray ray           = screenGame.getCamera().getPickRay(x, y);

        final HexMap currentMap = screenGame.getCurrentMap();
        if (currentMap == null
                || currentMap.getTiles() == null) {
            return null;
        }

        for (int xx=0; xx<currentMap.getTiles().length; xx++) {
            for (int yy=0; yy<currentMap.getTiles()[xx].length; yy++) {
                final RenderTile tile = currentMap.getTiles()[xx][yy].getRenderTile();

                final HexModel model = tile.getModel();
                model.getModelInstance().transform.getTranslation(position);
                //position.add((float) (HexagonUtil.hexagon.getSideLengthX()/2), 0.5f, (float) (HexagonUtil.hexagon.getSideLengthY()/2));
                //float distance = ray.origin.dst2(position);
                if (Intersector.intersectRaySphere(ray, position, 0.5f, null)) {
                    return model;
                }
            }
        }

        return null;
    }


    //private boolean flanke = false;

    public void select(HexModel model) {
        Vector3 pos = model.getModelInstance().transform.getTranslation(Vector3.Zero);
        pos.y += 0.02f;
        screenGame.selectedInstance.transform.setTranslation(pos);

        Point p = HexagonUtil.getArrayLocation(new TileLocation(Math.abs(pos.x) + 1, pos.z + 1));
        //System.out.println((Math.abs(pos.x) + 1) + ", " + (pos.z + 1) + " ---> " + p.getX() + ", " + p.getY());

        /*if (Gdx.input.isTouched() && !flanke) {
            AStar aStar = new AStar();
            int x;
            int y;
            do {
                x = (int) (Math.random() * 30);
                y = (int) (Math.random() * 30);
            } while (Math.abs(p.getX() - x) <= 5 && Math.abs(p.getY() - y) <= 5);
            List<AStar.ATile> path = aStar.start(new AStar.ATile(p.getX(), p.getY(), null), new AStar.ATile(x, y, null), screenGame.getCurrentMap());
            /*if (screenGame.car != null) {
                screenGame.car.getInstance().model.dispose();
            }* /
            screenGame.cars.add(new Car(path, 0));
            screenGame.cars.add(new Car(path, 1));
            screenGame.cars.add(new Car(path, 2));
            screenGame.cars.add(new Car(path, 3));
            screenGame.cars.add(new Car(path, 4));
            flanke = true;
        }
        if (!Gdx.input.isTouched()) {
            flanke = false;
        }*/
    }
}
