package fxzone.engine.utils;

import fxzone.engine.handler.AssetHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class ZoneMediaPlayer {

    private static final boolean verbose = true;

    private MediaPlayer mediaPlayer;

    public ZoneMediaPlayer(String path, boolean disposeOnEndOfMedia){
        init(AssetHandler.getSound(path), disposeOnEndOfMedia);
    }
    public ZoneMediaPlayer(Media media, boolean disposeOnEndOfMedia){
        init(media, disposeOnEndOfMedia);
    }
    public ZoneMediaPlayer(Media media) {
        init(media, false);
    }
    public ZoneMediaPlayer(String path){
        init(AssetHandler.getSound(path), false);
    }
    private void init(Media media, boolean disposeOnEndOfMedia){
        try{
            this.mediaPlayer = new MediaPlayer(media);
            if(disposeOnEndOfMedia){
                this.mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.dispose();
                    }
                });
            }
        }
        catch (NullPointerException e){
            if(verbose) System.err.println("[ZONE-MEDIA-PLAYER] Media initialized with null");
        }
        catch (MediaException e){
            System.err.println("[ZONE-MEDIA-PLAYER] ERROR on media player initialization");
        }
    }

    public void play(){
        if(this.mediaPlayer != null){
            this.mediaPlayer.play();
        } else {
            if(verbose) System.err.println("[ZONE-MEDIA-PLAYER] [play] ERROR");
        }
    }

    public void stop(){
        if(this.mediaPlayer != null){
            this.mediaPlayer.stop();
        } else{
            if(verbose) System.err.println("[ZONE-MEDIA-PLAYER] [stop] ERROR");
        }
    }

    public void setRate(double rate){
        if(this.mediaPlayer != null){
            this.mediaPlayer.setRate(rate);
        } else{
            if(verbose) System.err.println("[ZONE-MEDIA-PLAYER] [setRate] ERROR");
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void dispose(){
        this.mediaPlayer.dispose();
    }
}
