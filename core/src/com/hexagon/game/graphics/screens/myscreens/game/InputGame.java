package com.hexagon.game.graphics.screens.myscreens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.State;
import com.hexagon.game.graphics.screens.myscreens.game.GameStates.StateType;
import com.hexagon.game.input.HexInput;
import com.hexagon.game.map.HexMap;
import com.hexagon.game.map.Point;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.util.CameraHelper;
import com.hexagon.game.util.HexagonUtil;

/**
 * Created by Sven on 20.12.2017.
 */

public class InputGame extends HexInput {

    private static float    SPEED = 5;

    public CameraHelper    cameraHelper;

    private float           velX = 0;
    private float           velY = 0;
    private float           zoom = 0;

    private int             downX = 0;
    private int             downY = 0;

    /*private HexModel        selected;
    private Material        selectionMaterial;
    private Array<Material> originalMaterial;*/

    public Vector3         cameraLockOnTile = null;

    private ScreenGame      screenGame;

    public Point            selectedTile;

    public Vector3          hoverLocation = new Vector3(0, 0, 0);

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

        }
        if (keycode == Input.Keys.H) {
            screenGame.gameManager.connect(true);
        }
        if (keycode == Input.Keys.C) {
            screenGame.gameManager.connect(false);
        }
        if (keycode == Input.Keys.ENTER) {
            GameManager.instance.messageUtil.add("Halllllllooo " + ((int) (Math.random() * 150_000)));
            GameManager.instance.messageUtil.actionBar("Halllllllooo " + ((int) (Math.random() * 150_000)), 4000, Color.RED);
        }
        if (keycode == Input.Keys.P) {

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
        if (button == 1) {
            deselect();
        }
        HexModel model = getObject(screenX, screenY);
        if (model != null) {
            System.out.println("Down " + button);
            if (button == 0) {
                selectedTile = null;
                select(
                        hover(model)
                );
            }

        }
        downX = screenX;
        downY = screenY;


        //TODO: This needs to be multiplexed to the right System for the correct player!!!!!!!!!
        /*Engine.getInstance().BroadcastMessage(new NotificationNewEntity(
                Engine.getInstance().getEntityManager().createID(
                        new HexaComponentOwner("Player One",GameManager.instance.server.getLocalClientID()),
                        new HexaComponentOre()
                        )
                )
        );

        Engine.getInstance().BroadcastMessage(new NotificationNewEntity(
                        Engine.getInstance().getEntityManager().createID(
                                new HexaComponentOwner("Player One",GameManager.instance.server.getLocalClientID()),
                                new HexaComponentWood()
                        )
                )
        );


        Engine.getInstance().BroadcastMessage(new NotificationNewEntity(
                        Engine.getInstance().getEntityManager().createID(
                                new HexaComponentOwner("Player One",GameManager.instance.server.getLocalClientID()),
                                new HexaComponentStone()
                        )
                )
        );*/


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

    // Boolean for smoother zooming when reaching the max or min zooming border
    private boolean maxZoom = false;

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
            if (y < 2.0f) {
                if (!maxZoom) {
                    screenGame.getCamera().position.z += zoom;
                    maxZoom = true;
                }
                y = 2.0f;
            } else if (y > 11.0f) {
                if (!maxZoom) {
                    screenGame.getCamera().position.z += zoom;
                    maxZoom = true;
                }
                y = 11.0f;
            } else {
                maxZoom = false;
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
        if (camera.position.x < 0) {
            camera.position.x = 0;
        } else if (camera.position.x > screenGame.mapWidth) {
            camera.position.x = screenGame.mapWidth;
        }
        if (camera.position.z < 0) {
            camera.position.z = 0;
        } else if (camera.position.z > screenGame.mapHeight) {
            camera.position.z = screenGame.mapHeight;
        }

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
            hover(model);
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

    public Point hover(HexModel model) {

        if (screenGame.gameManager.getCurrentState().getStateType() == StateType.CITY_VIEW) return null;

        Vector3 pos = model.getModelInstance().transform.getTranslation(Vector3.Zero);

        Point p = HexagonUtil.getArrayLocation(new TileLocation(Math.abs(pos.x) + 1, pos.z + 1));
        Tile tile = screenGame.getCurrentMap().getTileAt(p);
        if (tile.getStructure() != null
                && tile.getStructure() instanceof StructureCity) {
            StructureCity city = (StructureCity) tile.getStructure();
            Color color = Color.WHITE;
            screenGame.gameManager.messageUtil.actionBar(city.getName(), 5000, color);
        }

        if (selectedTile != null) {
            return selectedTile;
        }
        pos.y += 0.02f;
        //screenGame.hoverInstance.transform.setTranslation(pos);
        hoverLocation.set(pos.x, pos.y, pos.z);
        return p;
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

    public void select(Point p) {
        if (screenGame.gameManager.getCurrentState().getStateType() == StateType.CITY_VIEW) return;
        if (p == null) return;

        if (p.equals(selectedTile)) {
            return;
        }

        selectedTile = p;
        //Sidebar window = screenGame.gameManager.sidebarBuildWindow;


        State currentState = GameManager.instance.getCurrentState();

        currentState.select(screenGame.getCurrentMap(), p, screenGame.getStage());

        //window.select(screenGame.getCurrentMap(), p, screenGame.getStage());

        /*GameManager.instance.server.send(new PacketBuild(
                p,
                StructureType.ORE,
                HexaServer.senderId
        ));*/
    }

    public void deselect() {
        if (screenGame.gameManager.getCurrentState().getStateType() == StateType.CITY_VIEW) return;
        selectedTile = null;

        State currentState = GameManager.instance.getCurrentState();
        currentState.deselect(screenGame.getStage());
    }

    /**
     * Deselects and selects the currently selected tile in order to update UI
     */
    public void updateSelectedInfo() {
        if (selectedTile != null) {
            Point p = selectedTile;
            screenGame.gameManager.getCurrentState().deselect(screenGame.getStage());
            screenGame.gameManager.getCurrentState().select(screenGame.getCurrentMap(), p, screenGame.getStage());
        }
    }

}
