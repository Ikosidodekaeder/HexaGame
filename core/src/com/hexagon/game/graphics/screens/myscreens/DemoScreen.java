package com.hexagon.game.graphics.screens.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexagon.game.Logic.Components.HexaComponentOwner;
import com.hexagon.game.Logic.Components.HexaComponentTest;
import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.Logic.Systems.HexaSystemGeneralConsumer;
import com.hexagon.game.Logic.Systems.HexaSystemGeneralProducer;
import com.hexagon.game.Logic.Systems.InterfaceSystem;
import com.hexagon.game.graphics.screens.HexagonScreen;
import com.hexagon.game.graphics.screens.ScreenType;


import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;

import de.svdragster.logica.components.Component;
import de.svdragster.logica.components.ComponentConsumer;
import de.svdragster.logica.components.ComponentConverter;
import de.svdragster.logica.components.ComponentExchanger;
import de.svdragster.logica.components.ComponentMailbox;
import de.svdragster.logica.components.ComponentProducer;
import de.svdragster.logica.components.ComponentProduct;
import de.svdragster.logica.components.ComponentResource;
import de.svdragster.logica.components.meta.ComponentType;
import de.svdragster.logica.components.meta.StdComponents;
import de.svdragster.logica.manager.Entity.Entity;
import de.svdragster.logica.manager.Entity.EntityManager;
import de.svdragster.logica.system.System;
import de.svdragster.logica.system.SystemMessageDelivery;
import de.svdragster.logica.util.SystemNotifications.NotificationNewEntity;
import de.svdragster.logica.util.SystemNotifications.NotificationRemoveEntity;
import de.svdragster.logica.world.Engine;

import static de.svdragster.logica.manager.Entity.EntityManager.createRawEntity;
import static java.lang.System.out;

/**
 * Created by Johannes Lüke on 18.12.2017.
 */

public class DemoScreen extends HexagonScreen {

    private static final Engine Logic = Engine.getInstance();

    private Thread LogicThread;
    private ShapeRenderer render;

    private Entity id ;

    public DemoScreen() {
        super(ScreenType.DEMOJoJo);
    }

    @Override
    public void create() {
        render = new ShapeRenderer();

        Logic.getSystemManager().addSystem(
                new SystemMessageDelivery(),
                new InterfaceSystem(),
                new HexaSystemGeneralProducer(Logic),
                new HexaSystemGeneralConsumer(Logic)
                );


        /**
         * EXAMPLE CODE READY FOR REMOVAL
         */

        /**
         * Producer
         */
            Entity id1 = createRawEntity(
                    new ComponentProducer(),
                    new ComponentResource(0.01f,3.0f,1.0f, Arrays.asList(
                            (Component) (new ComponentProduct())
                    )));
            id1.associateComponent(new HexaComponentOwner("Alpha",id1));


            Logic.getEntityManager().getEntityContext().add(id1);
            Logic.BroadcastMessage(new NotificationNewEntity(id1));
/////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * Producer
         */
            Entity id2 = createRawEntity(
                    new ComponentProducer(),
                    new ComponentResource(0.01f,3.5f,1.0f, Arrays.asList(
                            (Component) (new HexaComponentTest())
                    )));
            id2.associateComponent(new HexaComponentOwner("Beta",id2));


            Logic.getEntityManager().getEntityContext().add(id2);
            Logic.BroadcastMessage(new NotificationNewEntity(id2));
/////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * Consumer
         */
            Entity id3 = createRawEntity(
                    new ComponentConsumer(),
                    new ComponentResource(2.01f,1.0f,100.0f,  Arrays.asList(
                            (Component) (new ComponentProduct())
                    ))
            );
            id3.associateComponent(new HexaComponentOwner("Alpha",id1));

            Logic.getEntityManager().getEntityContext().add(id3);
            Logic.BroadcastMessage(new NotificationNewEntity(id3));

        /**
         * Consumer
         */
            Entity id4 = createRawEntity(
                    new ComponentConsumer(),
                    new ComponentResource(1.01f,1.0f,2.0f,  Arrays.asList(
                            (Component) (new HexaComponentTest())
                    ))
            );
            id4.associateComponent(new HexaComponentOwner("Beta",id2));

            Logic.getEntityManager().getEntityContext().add(id4);
            Logic.BroadcastMessage(new NotificationNewEntity(id4));


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        try {

        }catch (IllegalThreadStateException e){
            //e.printStackTrace();
        }

        Logic.run(Gdx.graphics.getRawDeltaTime());



       out.println("FPS: " + Gdx.app.getGraphics().getFramesPerSecond()
                          +" delta: " + Gdx.graphics.getRawDeltaTime() + " \t"
                          + " Logic FPS: "+ (long) Engine.FramesPerSecond()+ " \t" + " FrameTime(ns): " + Engine.FrameTime()  + " \t"
                          + " FrameTime(ms) " + Engine.FrameTime() / Math.pow(10,6) + "\t"
                          +" Entity Count: " + Logic.getEntityManager().getEntityContext().size());

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        try {
            LogicThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
