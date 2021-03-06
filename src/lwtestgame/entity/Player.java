/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.entity;

import lwtestgame.io.Window;
import lwtestgame.render.Animation;
import lwtestgame.render.Camera;
import lwtestgame.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;


/**
 *
 * @author sperr
 */
public class Player extends Entity {
    public static final int ANIM_IDLE = 0;
    public static final int ANIM_WALK = 1;
    public static final int ANIM_SIZE = 2;
    
    public Player(Transform transform) {
        super(ANIM_SIZE, transform);
        setAnimation(ANIM_IDLE, new Animation(4,2,"player/idle"));
        setAnimation(ANIM_WALK, new Animation(4,2,"player/walking"));
    }
 
    @Override
    public void update (float delta, Window window, Camera camera, World world) {
        Vector2f movement = new Vector2f();
        if ( (window.getInput().isKeyDown(GLFW_KEY_A)) || 
             (window.getInput().isKeyDown(GLFW_KEY_LEFT))){
            movement.add(-10*delta, 0);
        }
        if ( (window.getInput().isKeyDown(GLFW_KEY_D)) ||
             (window.getInput().isKeyDown(GLFW_KEY_RIGHT))){
            movement.add(10*delta, 0);
        }
        if ( (window.getInput().isKeyDown(GLFW_KEY_W)) ||
             (window.getInput().isKeyDown(GLFW_KEY_UP))){
            movement.add(0, 10*delta);
        }
        if ( (window.getInput().isKeyDown(GLFW_KEY_S)) ||
             (window.getInput().isKeyDown(GLFW_KEY_DOWN))){
            movement.add(0, -10*delta);
        } 
        
        move(movement);
        
        if (movement.x != 0 || movement.y != 0) 
            useAnimation(ANIM_WALK);
        else
            useAnimation(ANIM_IDLE);
            
        camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.5f);
        //camera.setPosition(transform.pos.mul(-world.getScale(), new Vector3f()));
    }
}
