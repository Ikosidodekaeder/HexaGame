package com.hexagon.game.util;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Sven on 12.02.2018.
 */

public class CameraHelper {

    private PerspectiveCamera camera;

    private boolean unlock = false; // Set to true if the camera should be unlocked when moving is completed
    private boolean locked = false;

    private Vector3         start;
    private Vector3         destination;
    private float           t; // location on vector between start and destination in percent (0.0f - 1.0f)
    private float           velocity;

    public CameraHelper(PerspectiveCamera camera) {
        this.camera = camera;
    }

    public void moveTo(Vector3 destination, boolean unlock) {
        this.destination    = destination.cpy();
        this.start          = camera.position.cpy();
        this.locked         = true;
        this.t              = 0;
        this.velocity       = 0.0005f;
        this.unlock         = unlock;
    }

    public float function(float x) {
        return (float) (0.1 * -(Math.pow(x - 0.5, 2)) + 0.0252);
    }

    public void update() {
        /*final float speed = 0.00001f;
        float dX = destination.x - camera.position.x;
        float dY = destination.y - camera.position.y;
        float dZ = destination.z - camera.position.z;
        double goalDistance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
        System.out.println(dX + ", " + dY + ", " + dZ + " /// " + goalDistance);
        if (goalDistance > speed) {
            float ratio = (float) (speed / goalDistance);
            camera.position.x = ratio*dX + destination.x;
            camera.position.y = ratio*dY + destination.y;
            camera.position.z = ratio*dZ + destination.z;
            camera.update();
        } else {
            camera.position.x = destination.x;
            camera.position.y = destination.y;
            camera.position.z = destination.z;
            camera.update();
            locked = false;
        }*/
        if (t > 1) {
            if (unlock) {
                locked = false;
            }
            return;
        }
        if (t <= 1) {
            t += velocity;
            /*if (t < 0.8f && velocity < 0.015f) {
                velocity *= (1+t*0.001f);
            } else if (t > 0.85f) {
                velocity = 0.1f * function(t);
            }*/

            velocity = function(t) * 1.5f;
            /*if (t <= 0.7f && velocity < 0.01f) {
                velocity = velocity * 1.3f + 0.005f;
            }*/
            if (unlock) {
                velocity *= 1.5f;
                if (velocity > 0.03f) {
                    velocity = 0.03f;
                }
            } else {
                if (velocity > 0.015f) {
                    velocity = 0.015f;
                }
            }
            System.out.println(t + " //// " + velocity);
        }
        if (t > 1) {
            if (unlock) {
                locked = false;
            }
            return;
        }
        camera.position.x = start.x * (1 - t) + destination.x * t;
        camera.position.y = start.y * (1 - t) + destination.y * t;
        camera.position.z = start.z * (1 - t) + destination.z * t;
        camera.update();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
