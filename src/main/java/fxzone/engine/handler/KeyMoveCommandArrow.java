package fxzone.engine.handler;

public class KeyMoveCommandArrow {
    public String keyPath;
    public boolean mirrorOnX;
    public boolean mirrorOnY;

    public KeyMoveCommandArrow(String keyPath, boolean mirrorOnX, boolean mirrorOnY){
        this.keyPath = keyPath;
        this.mirrorOnX = mirrorOnX;
        this.mirrorOnY = mirrorOnY;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyMoveCommandArrow))
            return false;
        KeyMoveCommandArrow ref = (KeyMoveCommandArrow) obj;
        return (this.keyPath.equals(ref.keyPath)) &&
            (this.mirrorOnX==ref.mirrorOnX) &&
            (this.mirrorOnY==ref.mirrorOnY);
    }

    @Override
    public int hashCode() {
        return keyPath.hashCode();
    }
}
