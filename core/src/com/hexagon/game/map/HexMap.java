package com.hexagon.game.map;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.hexagon.game.graphics.ModelManager;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureCity;
import com.hexagon.game.map.structures.StructureType;
import com.hexagon.game.map.structures.resources.StructureResource;
import com.hexagon.game.map.tiles.Biome;
import com.hexagon.game.map.tiles.Chunk;
import com.hexagon.game.map.tiles.Tile;
import com.hexagon.game.models.HexModel;
import com.hexagon.game.models.RenderTile;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.Player;
import com.hexagon.game.util.SettingsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sven on 08.12.2017.
 */

public class HexMap {

    public static int DEFAULT_JOBS = 100;

    private Hexagon hexagon; // reference hexagon

    private Tile[][] tiles;
    private Chunk[][] chunks;

    private transient List<StructureCity> cities = new ArrayList<>();
    // Tiles that someone has built on (for faster querying only)
    private transient List<Tile> builtTiles = new ArrayList<>();


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
                if (SettingsUtil.isLowGraphics()) {
                    // When using low graphics, don't display ICE and WATER tiles
                    Tile worldTile = tiles[arrayX][arrayY];
                    if (worldTile.getBiome() == Biome.WATER
                            || worldTile.getBiome() == Biome.ICE) {
                        continue;
                    }
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

    public int getJobs(UUID owner) {
        int jobs = DEFAULT_JOBS + GameManager.instance.jobModifier;
        for (int i=0; i<builtTiles.size(); i++) {
            Tile tile = builtTiles.get(i);
            if (tile.getOwner() != null
                    && tile.getOwner().equals(owner)
                    && tile.getStructure() != null) {
                if (tile.getStructure() instanceof StructureCity) {
                    jobs += ((StructureCity) tile.getStructure()).getJobs();
                } else {
                    jobs += tile.getStructure().getType().getJobs();
                }
            }
        }
        return jobs;
    }

    public void build(int x, int y, StructureType type, UUID owner) {
        Tile tile = GameManager.instance.getGame().getCurrentMap().getTileAt(x, y);
        RenderTile renderTile = tile.getRenderTile();
        TileLocation loc = tile.getTileLocation();

        if (owner != null) {
            if (GameManager.instance.server.isOfflineGame()
                    || (GameManager.instance.server.isHost() && owner.equals(HexaServer.senderId))) {
                // Only to handle the money and claim logic for the host
                if (tile.getOwner() == null) {
                    Player player = GameManager.instance.server.getSessionData().PlayerList.get(owner).getSecond();
                    if (player.claims <= 0) {
                        // If the player has no claims left, cancel the packet!
                        return;
                    }
                    int cost = GameManager.instance.getGame().getCurrentMap().getCostAt(new Point(x, y), type, owner);
                    if (player.money <= cost) {
                        // The player does not have enough money to buy this
                        return;
                    }
                    player.claims--;
                    player.money -= cost;
                }
            }

            HexModel colorModel = new HexModel(new ModelInstance(ModelManager.getInstance().getColorModel(
                    GameManager.instance.server.getSessionData().PlayerList.get(owner).getSecond().color
            )));
            colorModel.move((float) loc.getX(), 0.05f, (float) loc.getY());
            renderTile.setOwnerColor(colorModel);
            tile.setOwner(owner);

        }

        if (type == StructureType.FOREST || type == StructureType.FORESTRY) {
            Model treeModel = ModelManager.getInstance().getStructureModels(type).get(0);
            HexModel model = new HexModel(new ModelInstance(treeModel));
            model.move((float) loc.getX(), 0, (float) loc.getY());
            renderTile.getStructures().add(model);
            tile.setStructure(new Structure(type));
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
            renderTile.getStructures().clear();
            HexModel model = new HexModel(new ModelInstance(
                    ModelManager.getInstance().getStructureModels().get(StructureType.MINE).get(0)
            ));
            model.move((float) loc.getX(), 0.001f, (float) loc.getY());
            renderTile.getStructures().add(model);
            tile.setStructure(new Structure(type));
        } else if (type == StructureType.FACTORY) {
            HexModel model = new HexModel(new ModelInstance(
                    ModelManager.getInstance().getStructureModels().get(StructureType.FACTORY).get(0)
            ));
            model.move((float) loc.getX(), 0.001f, (float) loc.getY());
            renderTile.getStructures().add(model);
            tile.setStructure(new Structure(type));
        } else if (type == StructureType.STREET) {
            HexModel model = new HexModel(new ModelInstance(
                    ModelManager.getInstance().getStructureModels().get(StructureType.STREET).get(0)
            ));
            model.move((float) loc.getX(), 0.015f, (float) loc.getY());
            renderTile.getStructures().add(model);
            tile.setStructure(new Structure(type));
        } else if (type == StructureType.CROPS) {
            HexModel model = new HexModel(new ModelInstance(
                    ModelManager.getInstance().getStructureModels().get(StructureType.CROPS).get(0)
            ));
            model.move((float) loc.getX(), 0.005f, (float) loc.getY());
            renderTile.getStructures().add(model);
            tile.setStructure(new Structure(type));
        } else if (type == StructureType.QUARRY) {
            /*HexModel model = new HexModel(new ModelInstance(
                    ModelManager.getInstance().getStructureModels().get(StructureType.QUARRY).get(0)
            ));
            model.move((float) loc.getX(), 0.005f, (float) loc.getY());
            renderTile.getStructures().add(model);*/
            tile.setStructure(new Structure(type));
        }

        if (owner != null) {
            GameManager.instance.getGame().getCurrentMap().getBuiltTiles().add(tile);
        }
    }

    public void deconstruct(int x, int y) {
        Tile tile = GameManager.instance.getGame().getCurrentMap().getTileAt(x, y);
        RenderTile renderTile = tile.getRenderTile();
        tile.setStructure(null);
        renderTile.getStructures().clear();

        /*if (tile.getOwner() != null && builtTiles.contains(tile)) {
            builtTiles.remove(tile);
            if (GameManager.instance.server.isHost()) {
                Player player = GameManager.instance.server.getSessionData().PlayerList.get(tile.getOwner()).getSecond();
                player.claims++;
            }
        }*/
    }

    public boolean hasStructure(Point p, StructureType type) {
        Tile tile = getTileAt(p);
        return tile.getStructure() != null && tile.getStructure().getType() == type;
    }

    public boolean isNextTo(Point current, StructureType type) {
        Point north = new Point(current.x, current.y - 1);

        if (hasStructure(north, type)) {
            return true;
        }

        Point northEast;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, add 1 to x for north east
            northEast = new Point(current.x + 1, current.y);
        } else {
            // if x is EVEN, add 1 to x and remove 1 from y for north east
            northEast = new Point(current.x + 1, current.y - 1);
        }

        if (hasStructure(northEast, type)) {
            return true;
        }

        Point southEast;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, add 1 to x and 1 to y for south east
            southEast = new Point(current.x + 1, current.y + 1);
        } else {
            // if x is EVEN, add 1 to x for south east
            southEast = new Point(current.x + 1, current.y);
        }

        if (hasStructure(southEast, type)) {
            return true;
        }

        Point south = new Point(current.x, current.y + 1);

        if (hasStructure(south, type)) {
            return true;
        }

        Point southWest;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, remove 1 from x and add 1 to y for south west
            southWest = new Point(current.x - 1, current.y + 1);
        } else {
            // if x is EVEN, remove 1 from x for south west
            southWest = new Point(current.x - 1, current.y);
        }

        if (hasStructure(southWest, type)) {
            return true;
        }

        Point northWest;
        if ((current.x & 1) == 1) {
            // if x is UNEVEN, remove 1 from x for north west
            northWest = new Point(current.x - 1, current.y);
        } else {
            // if x is EVEN, remove 1 from x and y for north west
            northWest = new Point(current.x - 1, current.y - 1);
        }

        if (hasStructure(northWest, type)) {
            return true;
        }

        return false;
    }


    public int getCostAt(Point p, StructureType structureType) {
        return getCostAt(p, structureType, HexaServer.senderId);
    }

    public int getCostAt(Point p, StructureType structureType, UUID uuid) {
        int cost = structureType.getCost();
        if (isNextTo(p, StructureType.STREET)) {
            cost *= 0.75;
        } else if (isNextTo(p, StructureType.CITY)) {
            cost *= 1.5;
        }
        Point city = GameManager.instance.server.getSessionData().PlayerList.get(uuid).getSecond().cityLocation;
        if (city != null) {
            double distance = Math.pow(city.getX() - p.getX(), 2) + Math.pow(city.getY() - p.getY(), 2);
            if (distance >= 3 * 3) {
                cost += Math.sqrt(distance) * 10;
            }
        }
        return cost;
    }

    public List<StructureCity> getCities() {
        return cities;
    }

    public List<Tile> getBuiltTiles() {
        return builtTiles;
    }
}
