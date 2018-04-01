/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.assets;

import lwtestgame.render.Model;

/**
 *
 * @author sperr
 */
public class Assets {
    private static Model model;
    
    public static Model getModel() { return model; }
    
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
}
