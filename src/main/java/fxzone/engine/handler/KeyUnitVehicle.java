package fxzone.engine.handler;

public class KeyUnitVehicle {
    public String keyName;
    public Integer keyStance;

    public KeyUnitVehicle(String keyName, int keyStance) {
        this.keyName = keyName;
        this.keyStance = keyStance;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyUnitVehicle))
            return false;
        KeyUnitVehicle ref = (KeyUnitVehicle) obj;
        return this.keyName.equals(ref.keyName) &&
            this.keyStance.equals(ref.keyStance);
    }

    @Override
    public int hashCode() {
        return keyName.hashCode() ^ keyStance.hashCode();
    }
}
