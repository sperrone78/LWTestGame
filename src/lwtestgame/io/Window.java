/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.io;

import lwtestgame.io.Input;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import static org.lwjgl.opengl.GL11.GL_TRUE;

/**
 *
 * @author sperr
 */
public class Window {
    private long window;
    private int width, height;
    private boolean fullScreen;
    private Input input;
    
    public static void setCallbacks() {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
    }
    
    public Window() {
        setSize(640,480);
        setFullScreen(false);
    }
    
    public void createWindow(String title) {
        window = glfwCreateWindow(
                width, 
                height, 
                title,
                fullScreen ? glfwGetPrimaryMonitor(): 0,
                0);
        
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }
        
        if (!fullScreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window,
                    (vid.width()-width)/2,
                    (vid.height()-height)/2);
            glfwShowWindow(window);
        }
        glfwMakeContextCurrent(window);
        input = new Input(window);
    }
    
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }
    
    public void update() {
        input.update();
        glfwPollEvents();
    }
    
    public void swapBuffers() {
        glfwSwapBuffers(window);
    }
    
    public void setSize (int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }
    
    public long getWindow() {
        return window;
    }
    
    public Input getInput() {
        return input;
    }
}
