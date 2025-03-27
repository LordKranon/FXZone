package fxzone.engine.handler;

public class KeyCaptureBar {

    public int captureProgress;
    public java.awt.Color keyColor;

    public KeyCaptureBar(int captureProgress, java.awt.Color keyColor) {
        this.captureProgress = captureProgress;
        this.keyColor = keyColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyCaptureBar))
            return false;
        KeyCaptureBar ref = (KeyCaptureBar) obj;
        return (this.captureProgress == ref.captureProgress) &&
            (
                (this.keyColor == null && ref.keyColor == null) ||
                    ((this.keyColor != null && ref.keyColor != null) &&
                        (this.keyColor.equals(ref.keyColor)))
            );
    }

    @Override
    public int hashCode() {
        return keyColor.hashCode();
    }
}
