package fxzone.net.packet;

import fxzone.game.logic.serializable.UnitSerializable;

public class UnitCreatedPacket extends GameActionPacket{

    private final UnitSerializable unitSerializable;

    public UnitCreatedPacket(UnitSerializable unitSerializable) {
        super(PacketType.UNIT_CREATED);
        this.unitSerializable = unitSerializable;
    }

    public UnitSerializable getUnitSerializable(){
        return unitSerializable;
    }
}
