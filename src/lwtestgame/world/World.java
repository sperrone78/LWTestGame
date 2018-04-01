/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import lwtestgame.collision.AABB;
import lwtestgame.entity.Entity;
import lwtestgame.entity.Player;
import lwtestgame.entity.Transform;
import lwtestgame.io.Window;
import lwtestgame.render.Animation;
import lwtestgame.render.Camera;
import lwtestgame.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 *
 * @author sperr
 */
public class World {
    private byte[] tiles;
    private AABB[] boundingBoxes;
    private List<Entity> entities;
    private int width;
    private int height;
    private Matrix4f world;
    private int scale;
    private final int view = 48;
    
    public World (String world, Camera camera) {
        try {
            BufferedImage tileSheet = ImageIO.read(new File("./res/levels/"+ world + "/tiles.png"));
            BufferedImage entitySheet = ImageIO.read(new File("./res/levels/"+ world + "/entities.png"));
        
            width = tileSheet.getWidth();
            height = tileSheet.getHeight();
            scale = 16;
            
            this.world = new Matrix4f().setTranslation(new Vector3f(0));
            this.world.scale(scale);
            
            int[] colorTileSheet = tileSheet.getRGB(0,0,width, height, null, 0, width);
            int[] colorEntitySheet = entitySheet.getRGB(0,0,width, height, null, 0,width);
            
            tiles = new byte[width * height];
            boundingBoxes = new AABB[width * height];
            entities = new ArrayList<>();
            
            Transform transform;
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;
                    int entityIndex = (colorEntitySheet[x+y*width] >> 16) & 0xFF;
                    int entityAlpha = (colorEntitySheet[x+y*width] >> 24) & 0xFF;
                    
                    Tile t;
                    try {
                        t = Tile.tiles[red];
                        //System.out.println("I got a red at x = " + x + " and y at "+y);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //System.out.println("I got a red at x = " + x + " and y at " +y);
                        t = null;
                    }
                    
                    if (t != null)
                        setTile(t,x,y);
                    
                    if(entityAlpha > 0) {
                        transform = new Transform();
                        transform.pos.x = x*2;
                        transform.pos.y = -y*2;
                        switch(entityIndex) {
                            case 1:     //Player
                                Player player = new Player(transform);
                                entities.add(player);
                                camera.getPosition().set(transform.pos.mul(-scale, new Vector3f()));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            /*
            Transform t = new Transform();
            t.pos.x = 0;
            t.pos.y = -4;
            entities.add(new Entity(new Animation(1,1,"spaceShip"), t) {
                @Override
                public void update(float delta, Window window, Camera camera, World world) {
                    move(new Vector2f(5*delta,0));
                    
                }
            });
            */
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
    
    public World () {
        width = 128;
        height = 128;
        scale = 16;
        
        tiles = new byte[width * height];
        boundingBoxes = new AABB[width*height];
        world = new Matrix4f().setTranslation(new Vector3f(0));
        world.scale(scale);
    }
    
    public void render (TileRenderer render, Shader shader, Camera camera, Window window) {
       int posX = ((int)camera.getPosition().x + (window.getWidth()/2)) / (scale * 2);
       int posY = ((int)camera.getPosition().y - (window.getHeight()/2)) / (scale * 2);
       
       for (int i = 0; i< view; i++) {
           for (int j = 0; j < view; j++) {
               Tile t = getTile(i-posX, j + posY);
               if (t != null)
                   render.renderTile(t,i-posX, -j-posY, shader, world, camera);
           }
       }
       
       for (Entity entity : entities) {
           entity.render(shader, camera, this);
       } 
    }
    
    public void update (float delta, Window window, Camera camera) {
       for(Entity entity : entities) {
           entity.update(delta,window, camera, this);
       } 
        
       for (int i = 0; i < entities.size(); i++) {
           entities.get(i).collideWithTiles(this);
           for (int j = i+1; j<entities.size(); j++ ) {
               entities.get(i).collideWithEntity(entities.get(j));
           }
           entities.get(i).collideWithTiles(this);
       } 
    }
    
    public void correctCamera(Camera camera, Window window) {
        Vector3f pos = camera.getPosition();
        int w = -width * scale * 2;
        int h = height * scale * 2;
        
        if (pos.x > - (window.getWidth()/2)+ scale) {
            pos.x = - (window.getWidth()/2) + scale;
        }
        if (pos.x < w + (window.getWidth()/2)+ scale) {
            pos.x = w + (window.getWidth()/2) + scale;
        }
        if (pos.y < (window.getHeight()/2) - scale) {
            pos.y = (window.getHeight()/2) - scale;
        }
        if (pos.y > h - (window.getHeight()/2) - scale) {
            pos.y = h - (window.getHeight()/2) - scale;
        }
    }
    
    public void setTile(Tile tile, int x, int y) {
        tiles[x + y * width] = tile.getId();
        if (tile.isSolid()) {
            boundingBoxes[x+y*width] = new AABB(new Vector2f(x*2, -y*2), new Vector2f(1,1));
        } else {
            boundingBoxes[x+y*width] = null;
        }
    }
    
    public Tile getTile(int x, int y) {
        try {
            return Tile.tiles[tiles[x + y * width]];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public AABB getTileBoundingBox (int x, int y) {
        try {
            return boundingBoxes[x + y * width];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    public int getScale() {
        return scale;
    }
    
    public Matrix4f getWorldMatrix() {
        return world;
    }
}
