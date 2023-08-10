package fxzone.engine.handler;

import java.util.HashSet;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputHandler {

    private HashSet<KeyCode> keysPressed;

    public InputHandler(Scene scene){

        keysPressed = new HashSet<>();

        scene.setOnKeyPressed(keyEvent -> {
            keysPressed.add(keyEvent.getCode());
        });
        scene.setOnKeyReleased(keyEvent -> {
            keysPressed.remove(keyEvent.getCode());
        });
    }

    public boolean isKeyPressed(KeyCode keyCode){
        return keysPressed.contains(keyCode);
    }
}
