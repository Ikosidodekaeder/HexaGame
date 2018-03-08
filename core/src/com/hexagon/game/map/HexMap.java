package com.hexagon.game.map;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.hexagon.game.graphics.ModelManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.structures.resources.StructureResource;
import com.hexagon.game.map.tiles.Chunk;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.RenderTile;

import java.util.UUID;

/**
 * Created by Sven on 08.12.2017.
 */

public class HexMap {

    private Hexagon hexagon; // reference hexagon

    private Tile[][] tiles;
    private Chunk[][] chunks;


    public HexMap(int sizeX, int sizeY) {
        tiles = new Tile[sizeX][sizeY];
        System.out.println((sizeX >> 4) + ", " + (sizeY >> 4));
        chunks = new Chunk[(sizeX >> 4) + 1][(sizeY >> 4) + 1];
    }

    public void populateChunk(int chunkX, int chunkY) {
        Chunk chunk = chunks[chunkX][chunkY];
        if (chunk == null) {
            chunk = chunks[chunkX][chunkY] = new Chunk();
        }
        int tileX = chunkX << 4;
        int tileY = chunkY << 4;
        for (int x=0; x<16; x++) {
            for (int y=0; y<16; y++) {
                int arrayX = x + tileX;
                int arrayY = y + tileY;
                if (arrayX >= tiles.length
                        || arrayY >= tiles[arrayX].length) {
                    continue;
                }
                RenderTile tile = tiles[arrayX][arrayY].getRenderTile();
                chunk.getRenderTiles().add(tile);
            }
        }
    }

    public void populateChunks() {
        System.out.println("Populating chunks");
        for (int chunkX=0; chunkX<chunks.length; chunkX++) {
            for (int chunkY=0; chunkY<chunks[chunkX].length; chunkY++) {
                populateChunk(chunkX, chunkY);
            }
        }
        /*for (int x=0; x<tiles.length; x++) {
            int chunkX = x >> 4;
            for (int y=0; y<tiles[x].length; y++) {
                int chunkY = y >> 4;
                Chunk chunk = chunks[chunkX][chunkY];
                if (chunk == null) {
                    chunk = chunks[chunkX][chunkY] = new Chunk();
                }
                RenderTile tile = tiles[x][y].getRenderTile();
                chunk.getRenderTiles().add(tile);
            }
        }*/
        System.out.println("Updating");
        for (int x=0; x<chunks.length; x++) {
            for (int y = 0; y < chunks[x].length; y++) {
                Chunk chunk = chunks[x][y];
                chunk.updateBounds();
            }
        }
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTileAt(int x, int y) {
        if (x < 0 || y < 0
                || x > tiles.length
                || y > tiles[x].length) {
            return null;
        }
        return tiles[x][y];
    }

    public Tile getTileAt(Point point) {
        if (point.getX() < 0 || point.getY() < 0
                || point.getX() > tiles.length
                || point.getY() > tiles[point.getX()].length) {
            return null;
        }
        return tiles[point.getX()][point.getY()];
    }

    public Chunk[][] getChunks() {
        return chunks;
    }

    public void build(int x, int y, StructureType type, UUID owner) {
        Tile tile = GameManager.instance.getGame().getCurrentMap().getTileAt(x, y);
        RenderTile renderTile = tile.getRenderTile();
        TileLocation loc = tile.getTileLocation();

        if (owner != null) {
            HexModel colorModel = new HexModel(new ModelInstance(ModelManager.getInstance().getColorModel(
                    GameManager.instance.server.getSessionData().PlayerList.get(owner).getSecond().color
            )));
            colorModel.move((float) loc.getX(), 0.05f, (float) loc.getY());
            renderTile.setOwnerColor(colorModel);
            System.out.println("Color: " + GameManager.instance.server.getSessionData().PlayerList.get(owner).getSecond().toString());
        }

        if (type == StructureType.FOREST) {
            Model treeModel = ModelManager.getInstance().getStructureModels(StructureType.FOREST).get(0);
            HexModel model = new HexModel(new ModelInstance(treeModel));
            model.move((float) loc.getX(), 0, (float) loc.getY());
            renderTile.getStructures().add(model);
            tile.setStructure(new Structure(StructureType.FOREST));
            /*boolean placedTrees = false;
            if (Math.random() < 0.6) {
                HexModel model1 = new HexModel(new ModelInstance(treeModel));
                model1.move((float) loc.getX() + 0.3f, 0, (float) loc.getY() + 0.2f);
                renderTile.getStructures().add(model1);
                placedTrees = true;
            }
            if (Math.random() < 0.6) {
                HexModel model2 = new HexModel(new ModelInstance(treeModel));
                model2.move((float) loc.getX() - 0.3f, 0, (float) loc.getY());
                renderTile.getStructures().add(model2);
                placedTrees = true;

            }
            if (!placedTrees || Math.random() < 0.6) {
                HexModel model3 = new HexModel(new ModelInstance(treeModel));
                model3.move((float) loc.getX() + 0.3f, 0, (float) loc.getY() - 0.3f);
                renderTile.getStructures().add(model3);
            }

            if (tile.getStructure() == null) {
                tile.setStructure(new Structure(StructureType.FOREST));
            }*/

        } else if (type == StructureType.ORE) {
            StructureResource resource = (StructureResource) tile.getStructure();
            HexModel model = new HexModel(new ModelInstance(
                    ModelManager.getInstance().getStructureModels().get(StructureType.ORE).get(0)
            ));
            model.move((float) loc.getX(), 0.01f, (float) loc.getY());
            renderTile.getStructures().add(model);
        } else if (type == StructureType.CITY) {
            StructureCity structureCity = (StructureCity) tile.getStructure();
            HexModel model = new HexModel(
                    new ModelInstance(
                            ModelManager.getInstance()
                            .getStructureModels(type)
                            .get(structureCity.getLevel())
                    )
            );
            model.move((float) loc.getX(), 0.05f, (float) loc.getY());
            renderTile.getStructures().add(model);
        } else if (type == StructureType.MINE) {
            HexModel model = new HexModel(new ModelInstance(
                    ModelManager.getInstance().getStructureModels().get(StructureType.MINE).get(0)
            ));
            model.move((float) loc.getX(), 0.001f, (float) loc.getY());
            renderTile.getStructures().add(model);
        }
    }

    public void deconstruct(int x, int y) {
        Tile tile = GameManager.instance.getGame().getCurrentMap().getTileAt(x, y);
        RenderTile renderTile = tile.getRenderTile();
        tile.setStructure(null);
        renderTile.getStructures().clear();
    }
}
