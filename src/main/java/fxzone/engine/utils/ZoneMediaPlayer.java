package fxzone.engine.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class ZoneMediaPlayer {

    private static final boolean verbose = true;

    private MediaPlayer mediaPlayer;

    public ZoneMediaPlayer(Media media) {
        try{
            this.mediaPlayer = new MediaPlayer(media);
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
}
