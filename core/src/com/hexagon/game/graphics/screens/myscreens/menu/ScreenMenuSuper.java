package com.hexagon.game.graphics.screens.myscreens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.hexagon.game.graphics.ModelManager;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;

/**
 * Created by Sven on 13.03.2018.
 */

public abstract class ScreenMenuSuper extends HexagonScreen {

    private ModelBatch      modelBatch;
    private Model           modelScenery;
    private ModelInstance   instanceScenery;
    private Environment     environment;

    private PerspectiveCamera camera;

    public ScreenMenuSuper(ScreenType screenType) {
        super(screenType);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void create() {
        modelBatch = new ModelBatch();

        modelScenery = ModelManager.getInstance().loadModel("flat_hexagon.g3db");

        instanceScenery = new ModelInstance(modelScenery);
        instanceScenery.transform.setToTranslation(0, 0, 0);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
        //environment.shadowMap = new DirectionalShadowLight(1024*2, 1024*2, 60f*4, 60f*4, 1f, 300f);

        camera = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 5, 0);
        camera.lookAt(0, 0, 0);
        camera.near = 1;
        camera.far = 300;
        camera.update();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }

    @Override
    public void render(float delta) {
        ScreenManager.getInstance().clearScreen(0.0f, 0.25f, 0.35f);

        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_FRONT);

        modelBatch.begin(camera);
        modelBatch.render(instanceScenery, environment);
        modelBatch.end();
    }
}
