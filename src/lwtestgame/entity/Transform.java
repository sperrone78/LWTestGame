/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author sperr
 */
public class Transform {
    //can include rotation too
    public Vector3f pos;
    public Vector3f scale;
    
    public Transform() {
        pos = new Vector3f();
        scale = new Vector3f(1,1,1);
    }
    
    public Matrix4f getProjection(Matrix4f target) {
        target.translate(pos);
        target.scale(scale);
        return target;
    }
}
