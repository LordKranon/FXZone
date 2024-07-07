package fxzone.game.logic;

import fxzone.game.logic.serializable.TileSpaceObjectSerializable;
import fxzone.game.render.GameObjectInTileSpace;
import javafx.scene.Group;

public class TileSpaceObject {

    /**
     * Tile position in map.
     * min = 0; max = map width/height - 1
     */
    int x, y;

    /**
     * Graphical representation of this tile space object.
     */
    GameObjectInTileSpace gameObjectInTileSpace;


    /**
     * Constructor
     *
     * @param x game logical tile position in the map
     * @param y game logical tile position in the map
     */
    public TileSpaceObject(int x, int y){
        this.x = x;
        this.y = y;
    }

    public TileSpaceObject(TileSpaceObjectSerializable tileSpaceObjectSerializable){
        this.x = tileSpaceObjectSerializable.x;
        this.y = tileSpaceObjectSerializable.y;
    }

    public void setGraphicalOffset(double offsetX, double offsetY){
        this.gameObjectInTileSpace.setOffset(offsetX, offsetY);
    }
    public void changeTileRenderSize(Map map){
        this.gameObjectInTileSpace.changeTileRenderSize(x, y, map);
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setPositionInMap(int x, int y, Map map){
        this.x = x;
        this.y = y;
        gameObjectInTileSpace.setPositionInMap(x, y, map);
    }

    public void onRemoval(Group group){
        gameObjectInTileSpace.removeSelfFromRoot(group);
    }

    public void setVisible(boolean visible){
        this.gameObjectInTileSpace.setVisible(visible);
    }
}
