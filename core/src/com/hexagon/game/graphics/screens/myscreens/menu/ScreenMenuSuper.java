package com.hexagon.game.graphics.screens.myscreens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.hexagon.game.graphics.ModelManager;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.util.CameraHelper;

/**
 * Created by Sven on 13.03.2018.
 */

public class ScreenMenuSuper {

    private ModelBatch      modelBatch;
    private Model[]         modelScenery = new Model[4];
    private ModelInstance[] instanceScenery = new ModelInstance[4];
    private Environment     environment;

    private PerspectiveCamera camera;

    private CameraHelper    cameraHelper;

    public ScreenMenuSuper() {
        create();
    }

    @SuppressWarnings("deprecation")
    public void create() {
        modelBatch = new ModelBatch();

        modelScenery[0] = ModelManager.getInstance().loadModel("sc_buildings.g3db");
        //modelScenery[1] = ModelManager.getInstance().loadModel("sc_grass.g3db");
        modelScenery[2] = ModelManager.getInstance().loadModel("sc_terrain.g3db");
        modelScenery[3] = ModelManager.getInstance().loadModel("sc_trees.g3db");

        for (int i=0; i<modelScenery.length; i++) {
            if (i == 1) continue;
            instanceScenery[i] = new ModelInstance(modelScenery[i]);
            //instanceScenery[i].transform.setToTranslation(0, 0, 0);
        }

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        environment.add(new DirectionalLight().set(0.2f, 0.5f, 0.6f, 0f, -0.8f, -0.2f));
        //environment.shadowMap = new DirectionalShadowLight(1024*2, 1024*2, 60f*4, 60f*4, 1f, 300f);

        camera = new PerspectiveCamera(55, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(5, 1, 3);
        camera.lookAt(-2, 0, 1);
        camera.near = 1;
        camera.far = 300;
        camera.update();

        camera.position.set(5, 1, 3);
        cameraHelper = new CameraHelper(camera, 0.07f, 0.003f);
        cameraHelper.moveTo(new Vector3(-2, 1.5f, 4), false);
    }

    public void show() {

    }

    public void dispose() {
        modelBatch.dispose();
    }

    public void render(float delta) {
        ScreenManager.getInstance().clearScreen(0.0f, 0.25f, 0.35f);

        if (cameraHelper != null) {
            camera.lookAt(-3, 1, 1);
            cameraHelper.update();
        }

        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glEnable(GL20.GL_BLEND); // allows transparent drawing
        Gdx.gl.glCullFace(GL20.GL_FRONT);

        modelBatch.begin(camera);
        for (int i=0; i<instanceScenery.length; i++) {
            if (i == 1) continue;
            modelBatch.render(instanceScenery[i], environment);
        }
        modelBatch.end();

    }
}
