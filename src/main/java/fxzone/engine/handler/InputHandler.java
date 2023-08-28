package fxzone.engine.handler;

import java.util.HashSet;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputHandler {

    private HashSet<KeyCode> keysPressed;

    private HashSet<MouseButton> mouseButtonsPressed;

    private Point2D lastMousePosition;

    /**
     * Used for handling mouse clicks. A "click" is when the left mouse button enters the "pressed" position.
     */
    private Point2D lastMousePrimaryButtonPressedPosition;
    private boolean mousePrimaryButtonPressProcessed = true;

    private double cumulativeScrollDelta;


    public InputHandler(Scene scene){

        keysPressed = new HashSet<>();

        scene.setOnKeyPressed(keyEvent -> {
            keysPressed.add(keyEvent.getCode());
        });
        scene.setOnKeyReleased(keyEvent -> {
            keysPressed.remove(keyEvent.getCode());
        });


        mouseButtonsPressed = new HashSet<>();

        scene.setOnMousePressed(mouseEvent -> {
            mouseButtonsPressed.add(mouseEvent.getButton());
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                lastMousePrimaryButtonPressedPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                mousePrimaryButtonPressProcessed = false;
            }
        });
        scene.setOnMouseReleased(mouseEvent -> {
            mouseButtonsPressed.remove(mouseEvent.getButton());
        });

        scene.setOnMouseMoved(mouseEvent -> {
            lastMousePosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        });

        scene.setOnScroll(scrollEvent -> {
            cumulativeScrollDelta += scrollEvent.getDeltaY();
        });
    }

    public boolean isKeyPressed(KeyCode keyCode){
        return keysPressed.contains(keyCode);
    }
    public boolean isMouseButtonPressed(MouseButton mouseButton){
        return mouseButtonsPressed.contains(mouseButton);
    }
    public Point2D getLastMousePosition(){
        return new Point2D(lastMousePosition.getX(), lastMousePosition.getY());
    }
    public double getCumulativeScrollDelta(){
        double returnedScrollDelta = cumulativeScrollDelta;
        cumulativeScrollDelta = 0;
        return returnedScrollDelta;
    }
    public boolean wasMousePrimaryButtonPressed(){
        return !mousePrimaryButtonPressProcessed;
    }
    public Point2D getLastMousePrimaryButtonPressedPosition(){
        mousePrimaryButtonPressProcessed = true;
        return new Point2D(lastMousePrimaryButtonPressedPosition.getX(), lastMousePrimaryButtonPressedPosition.getY());
    }
}
