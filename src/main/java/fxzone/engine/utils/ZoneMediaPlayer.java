package fxzone.engine.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class ZoneMediaPlayer {

    private MediaPlayer mediaPlayer;

    public ZoneMediaPlayer(Media media) {
        try{
            this.mediaPlayer = new MediaPlayer(media);
        } catch (MediaException e){
            System.err.println("[ZONE-MEDIA-PLAYER] ERROR on media player initialization");
        }
    }

    public void play(){
        if(this.mediaPlayer != null){
            this.mediaPlayer.play();
        } else {
            System.err.println("[ZONE-MEDIA-PLAYER] [play] ERROR");
        }
    }

    public void stop(){
        if(this.mediaPlayer != null){
            this.mediaPlayer.stop();
        } else{
            System.err.println("[ZONE-MEDIA-PLAYER] [stop] ERROR");
        }
    }

    public void setRate(double rate){
        if(this.mediaPlayer != null){
            this.mediaPlayer.setRate(rate);
        } else{
            System.err.println("[ZONE-MEDIA-PLAYER] [setRate] ERROR");
        }
    }
}
