package com.hexagon.game.map.tiles;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.hexagon.game.map.TileLocation;
import com.hexagon.game.map.detail.Car;
import com.hexagon.game.map.structures.IStructure;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.util.HexagonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * Stores data for a tile
 *
 * Created by Sven on 08.12.2017.
 */

public class Tile {

    private transient RenderTile      renderTile; // RenderTile stores rendering data
    private transient TileLocation    tileLocation;
    private int             arrayX;
    private int             arrayY;

    private Biome           biome = Biome.ICE;
    private IStructure      structure;
    private UUID            owner;

    private transient List<Car>       cars = new ArrayList<>();


    public TileLocation getTileLocation() {
        return tileLocation;
    }

    public Tile() {
        // Gson requires this constructor
    }

    public Tile(int arrayX, int arrayY) {
        this.arrayX = arrayX;
        this.arrayY = arrayY;
        this.tileLocation = HexagonUtil.getTileLocation(arrayX, arrayY);
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        renderTile.render(modelBatch, environment);
    }

    public void setTileLocation(TileLocation tileLocation) {
        this.tileLocation = tileLocation;
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public IStructure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public RenderTile getRenderTile() {
        return renderTile;
    }

    public void setRenderTile(RenderTile renderTile) {
        this.renderTile = renderTile;
    }

    public int getArrayX() {
        return arrayX;
    }

    public int getArrayY() {
        return arrayY;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void updateTileLocation() {
        this.tileLocation = HexagonUtil.getTileLocation(arrayX, arrayY);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }
}
