package fxzone.engine.handler;

import fxzone.engine.controller.AbstractGameController;
import java.util.HashSet;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputHandler {

    private AbstractGameController gameController;

    private final HashSet<KeyCode> keysPressed;

    private final HashSet<MouseButton> mouseButtonsPressed;

    private Point2D lastMousePosition;

    /**
     * Used for handling mouse clicks. A "click" is when the left mouse button enters the "pressed" position.
     */
    private Point2D lastMousePrimaryButtonPressedPosition;
    private boolean mousePrimaryButtonPressProcessed = true;

    private Point2D lastMouseSecondaryButtonPressedPosition;
    private boolean mouseSecondaryButtonPressProcessed = true;

    private double cumulativeScrollDelta;


    public InputHandler(AbstractGameController gameController, Scene scene){

        this.gameController = gameController;
        keysPressed = new HashSet<>();

        scene.setOnKeyPressed(keyEvent -> {
            if(!keysPressed.contains(keyEvent.getCode())){
                gameController.keyPressed(keyEvent.getCode());
            }
            keysPressed.add(keyEvent.getCode());
        });
        scene.setOnKeyReleased(keyEvent -> {
            if(keysPressed.contains(keyEvent.getCode())){
                gameController.keyReleased(keyEvent.getCode());
            }
            keysPressed.remove(keyEvent.getCode());
        });


        mouseButtonsPressed = new HashSet<>();

        scene.setOnMousePressed(mouseEvent -> {
            mouseButtonsPressed.add(mouseEvent.getButton());
            if(mouseEvent.getButton() == MouseButton.PRIMARY){
                lastMousePrimaryButtonPressedPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                mousePrimaryButtonPressProcessed = false;
            } else if(mouseEvent.getButton() == MouseButton.SECONDARY){
                lastMouseSecondaryButtonPressedPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                mouseSecondaryButtonPressProcessed = false;
            }
        });
        scene.setOnMouseReleased(mouseEvent -> {
            mouseButtonsPressed.remove(mouseEvent.getButton());
        });

        scene.setOnMouseMoved(mouseEvent -> {
            lastMousePosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        });
        scene.setOnMouseDragged(mouseEvent -> {
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
    public boolean wasMouseSecondaryButtonPressed(){
        return !mouseSecondaryButtonPressProcessed;
    }
    public Point2D getLastMouseSecondaryButtonPressedPosition(){
        mouseSecondaryButtonPressProcessed = true;
        return new Point2D(lastMouseSecondaryButtonPressedPosition.getX(), lastMouseSecondaryButtonPressedPosition.getY());
    }
}
