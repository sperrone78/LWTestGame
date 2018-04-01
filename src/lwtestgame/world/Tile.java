/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lwtestgame.world;

/**
 *
 * @author sperr
 */
public class Tile {
    public static Tile tiles[] = new Tile[255];
    public static byte numTiles = 0;
    public static final Tile grassTile = new Tile("/tiles/grass");
    public static final Tile concreteTile = new Tile("/tiles/concrete").setSolid();
    public static final Tile dirtTile = new Tile("/tiles/dirt");
    public static final Tile roadTile = new Tile("/tiles/road");
    public static final Tile spaceshipTile = new Tile("/tiles/Spaceship");
    public static final Tile blueCamoTile = new Tile("/tiles/blueCamo");
    
    private byte id;
    private boolean solid;
    private String texture;
    
    public Tile (String texture) {
        this.id = numTiles;
        numTiles ++ ;
        this.texture = texture;
        this.solid = false;
        if (tiles[id] != null) {
            throw new IllegalStateException("Tiles at [" + id + "] is already being used");
        }
        tiles[id] = this;
    }

    public Tile setSolid() { 
        this.solid = true; return this; 
    }
    
    public boolean isSolid () {
        return solid;
    }
    
    public static Tile[] getTiles() {
        return tiles;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
