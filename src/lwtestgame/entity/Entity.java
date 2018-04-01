/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.entity;

import lwtestgame.collision.AABB;
import lwtestgame.collision.Collision;
import lwtestgame.io.Window;
import lwtestgame.render.Animation;
import lwtestgame.render.Camera;
import lwtestgame.render.Model;
import lwtestgame.render.Shader;
import lwtestgame.render.Texture;
import lwtestgame.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

/**
 *
 * @author sperr
 */
public abstract class Entity {
    private static Model model;
    //private Texture texture;
    protected AABB boundingBox;
    protected Animation[] animations;
    protected Transform transform;
    private int useAnimation;
    
    public Entity (int maxAnimations, Transform transform) {
        //this.texture = new Texture("playerShip.png");
        this.animations = new Animation[maxAnimations];
        
        this.transform = transform;
        this.useAnimation = 0;
        
        boundingBox = new AABB(
                new Vector2f(transform.pos.x, transform.pos.y), 
                new Vector2f(transform.scale.x, transform.scale.y)
        );
    }
    
    protected void setAnimation(int index, Animation animation) {
		animations[index] = animation;
    }
    
    public void useAnimation(int index) {
        this.useAnimation = index;
    }
    
    public void move(Vector2f direction) {
        transform.pos.add(new Vector3f(direction, 0));
        boundingBox.getCenter().set(transform.pos.x, transform.pos.y);
    }
    
    public abstract void update (float delta, Window window, Camera camera, World world);
    
    public void collideWithTiles(World world) {
        AABB[] boxes = new AABB[25];
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i+j*5] = world.getTileBoundingBox(
                        (int)(((transform.pos.x /2) + 1f) - (5/2)) + i,
                        (int)(((-transform.pos.y /2) + 1f) - (5/2)) + j
                );
            }
        }
        
        AABB box = null;
        for(int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (box == null) box = boxes[i];
                
                Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                
                if (length1.lengthSquared() > length2.lengthSquared()) {
                    box = boxes[i];
                }
            }
        }
        
        if (box != null) {
            Collision data = boundingBox.getCollision(box);
            if (data.isIntersecting) {
                boundingBox.correctPosition(box,data);
                transform.pos.set(boundingBox.getCenter(), 0);
            } 
            
            for(int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null) {
                if (box == null) box = boxes[i];
                
                Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                
                if (length1.lengthSquared() > length2.lengthSquared()) {
                    box = boxes[i];
                }
            }
        }
            data = boundingBox.getCollision(box);
            if (data.isIntersecting) {
                boundingBox.correctPosition(box,data);
                transform.pos.set(boundingBox.getCenter(), 0);
            } 
        }
    }
    
    public void render (Shader shader, Camera camera, World world) {
        Matrix4f target = camera.getProjection();
        target.mul(world.getWorldMatrix());
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", transform.getProjection(target));
        animations[useAnimation].bind(0);
        model.render();
    }
    
    public static void initAsset () {
        float[] vertices = new float[] {
            -1f, 1f, 0, //TOP Left  0
            1f, 1f, 0, // Top Right 1
            1f, -1f,0, // Bottom Right 2
            -1f, -1f,0, //Bottom Left 3

        };
        float [] texture = new float[] {
            0,0,
            1,0,
            1,1,
            0,1,
        };
        
        int[] indices = new int[] {
            0,1,2,
            2,3,0
        };  
        model = new Model(vertices, texture, indices);
    }
    
    public static void deleteAsset() {
        model = null;
    }

    public void collideWithEntity(Entity entity) {
        Collision collision = boundingBox.getCollision(entity.boundingBox);
        
        if(collision.isIntersecting) {
            collision.distance.x /= 2;
            collision.distance.y /= 2;
           
            boundingBox.correctPosition(entity.boundingBox, collision);
            transform.pos.set(boundingBox.getCenter().x, boundingBox.getCenter().y,0);
        
            entity.boundingBox.correctPosition(boundingBox, collision);
            entity.transform.pos.set(entity.boundingBox.getCenter().x, entity.boundingBox.getCenter().y,0);
        }
    }
}
