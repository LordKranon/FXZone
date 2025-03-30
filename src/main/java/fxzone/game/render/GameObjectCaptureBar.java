package fxzone.game.render;

import fxzone.engine.handler.AssetHandler;
import fxzone.engine.handler.KeyCaptureBar;
import fxzone.game.logic.Codex;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class GameObjectCaptureBar extends GameObjectInTileSpace{

    private final Image[] progressImages = new Image[Codex.BUILDING_CAPTURE_TOTAL + 1];

    public GameObjectCaptureBar(int x, int y, double tileRenderSize, Group group) {
        super(null, x, y, tileRenderSize, group);
    }

    public void initToPlayer(java.awt.Color color, int progress){
        for (int i = 0; i < (Codex.BUILDING_CAPTURE_TOTAL + 1); i++){
            progressImages[i] = AssetHandler.getImageCaptureBar(new KeyCaptureBar(i, color));
        }
        setShownProgress(progress);
    }
    public void setShownProgress(int progress){
        if(progress > Codex.BUILDING_CAPTURE_TOTAL){
            progress = Codex.BUILDING_CAPTURE_TOTAL;
        } else if(progress < 0){
            progress = 0;
        }
        setImage(progressImages[progress]);
    }
}
