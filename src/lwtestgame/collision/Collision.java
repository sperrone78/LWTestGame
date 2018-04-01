/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.collision;

import org.joml.Vector2f;

/**
 *
 * @author sperr
 */
public class Collision {
    public Vector2f distance;
    public boolean isIntersecting;
    
    public Collision (Vector2f distance, boolean intersects) {
        this.distance = distance;
        this.isIntersecting = intersects;
    }
}
