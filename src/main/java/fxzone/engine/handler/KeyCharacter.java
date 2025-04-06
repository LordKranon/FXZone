package fxzone.engine.handler;

import fxzone.game.logic.Codex.CharacterType;

public class KeyCharacter {

    public CharacterType keyCharacterType;
    public java.awt.Color keyColor;

    public KeyCharacter(CharacterType keyCharacterType, java.awt.Color keyColor) {
        this.keyCharacterType = keyCharacterType;
        this.keyColor = keyColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyCharacter))
            return false;
        KeyCharacter ref = (KeyCharacter) obj;
        return this.keyCharacterType.equals(ref.keyCharacterType) &&
            (
                (this.keyColor == null && ref.keyColor == null) ||
                    ((this.keyColor != null && ref.keyColor != null) &&
                        (this.keyColor.equals(ref.keyColor)))
            );
    }

    @Override
    public int hashCode() {
        return keyCharacterType.hashCode() ^ keyColor.hashCode();
    }
}
