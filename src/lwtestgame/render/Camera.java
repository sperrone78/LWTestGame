/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author sperr
 */
public class Camera {
    private Vector3f position;
    private Matrix4f projection;
    
    public Camera(int width, int height) {
        position = new Vector3f(0,0,0);
        setProjection (width, height);
    }
    
    public void setPosition (Vector3f position) {
        this.position = position;
    }
    
    public void addPosition (Vector3f postion) {
        this.position.add(position);
    }
    
    public Vector3f getPosition() { return position; }
    
    public Matrix4f getProjection() { 
        return projection.translate(position, new Matrix4f());
    }
    
    public Matrix4f getUntransformedProjection() {
        return projection;
    }
    
    public void setProjection(int width, int height) {
        projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
    }
}
