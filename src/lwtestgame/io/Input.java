/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.io;
import static org.lwjgl.glfw.GLFW.*;
/**
 *
 * @author sperr
 */
public class Input {
    private long window;
    private boolean keys[];
    
    
    public Input(long window) {
        this.window = window;
        this.keys = new boolean[GLFW_KEY_LAST];
        for(int i = 32; i < GLFW_KEY_LAST; i++) {
            keys[i] = false;
        }
    }
    
    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == 1;
    }
    
    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }
    
    public boolean isKeyReleased(int key) {
        return (!isKeyDown(key) && keys[key]);
    }
    
    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == 1;
    }
    
    public void update() {
        for(int i = 32; i < GLFW_KEY_LAST; i++) {
            keys[i] = isKeyDown(i);
        }
    }
    

    
}
