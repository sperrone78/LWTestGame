/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.render;

import lwtestgame.io.Timer;

/**
 *
 * @author sperr
 */
public class Animation {
    private Texture[] frames;
    private int pointer;
    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;
    
    public Animation(int amount, int fps, String filename) {
        //can add String Ext if want something other than png
        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0/(double)fps;
        
        this.frames = new Texture[amount];
        for (int i = 0; i < amount ; i++) {
            System.out.println(filename+i);
            this.frames[i] = new Texture(filename+"/"+i+".png");
        }
                
    }
    public void bind() {bind(0); }
    public void bind(int sampler) {
        this.currentTime = Timer.getTime();
        this.elapsedTime += currentTime - lastTime;
        
        if (elapsedTime >= fps) {
            elapsedTime = 0;
            pointer++;
        }
        
        if (pointer>=frames.length) pointer = 0;
        this.lastTime = currentTime;
        
        frames[pointer].bind(sampler);
    }
}
