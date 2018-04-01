/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.game;

import lwtestgame.assets.Assets;
import lwtestgame.collision.AABB;
import lwtestgame.entity.Entity;
import lwtestgame.entity.Player;
import lwtestgame.entity.Transform;
import lwtestgame.gui.Gui;
import lwtestgame.render.Texture;
import lwtestgame.io.Timer;
import lwtestgame.io.Window;
import lwtestgame.render.Shader;
import lwtestgame.render.Model;
import lwtestgame.render.Camera;
import lwtestgame.world.Tile;
import lwtestgame.world.TileRenderer;
import lwtestgame.world.World;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;



/**
 *
 * @author sperr
 */
public class LWTestGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Window.setCallbacks();
        
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        if (!glfwInit()) {
            throw new IllegalStateException ("Failed to initialize GLFW!");
        }
        
        Window window = new Window();
        window.setSize(1280, 800);
        //window.setFullScreen(true);
        window.createWindow("Game");
        
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        Camera camera = new Camera(window.getWidth(), window.getHeight());
        glEnable(GL_TEXTURE_2D);
        
        TileRenderer tiles = new TileRenderer();
        Assets.initAsset();
        Shader shader = new Shader("shader");
        
        World world = new World("testLevel", camera);
        world.calculateView(window);
        
        Gui gui = new Gui(window);
        
        
        double frameCap = 1.0/60.0;
        double frameTime = 0;
        int frames = 0;
        
        double time = Timer.getTime();
        double unprocessed = 0;
        
        while (!window.shouldClose()) {
            boolean canRender = false;
            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed+=passed;
            frameTime += passed;
            time = time2;
            
            while (unprocessed >= frameCap) {
                if (window.hasResized()) {
                    camera.setProjection (window.getWidth(), window.getHeight());
                    gui.resizeCamera(window);
                    world.calculateView(window);
                    glViewport(0,0,window.getWidth(), window.getHeight());
                }
                unprocessed -= frameCap;
                canRender = true;
                //target = scale;
                if(window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    GLFW.glfwSetWindowShouldClose(window.getWindow(), true); 
                }
                
                world.update((float)frameCap, window, camera);
                
                world.correctCamera(camera, window);
                window.update();
                if (frameTime >= 1.0) {
                    frameTime = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);
                //tex.bind(0);
                //shader.bind();
                //shader.setUniform("sampler", 0);
                //shader.setUniform("projection", camera.getProjection().mul(target));
                //model.render();
                world.render(tiles, shader, camera);
                gui.render();
                window.swapBuffers();
                frames++;
            }

        }
        Assets.deleteAsset();
        glfwTerminate();
    }
}
