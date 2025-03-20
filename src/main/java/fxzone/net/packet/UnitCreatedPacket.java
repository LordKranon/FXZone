package fxzone.net.packet;

import fxzone.game.logic.serializable.UnitSerializable;

public class UnitCreatedPacket extends GameActionPacket{

    private final UnitSerializable unitSerializable;

    private final int statPurchasingPrice;

    private final boolean inTransport;

    public UnitCreatedPacket(UnitSerializable unitSerializable, int statPurchasingPrice, boolean inTransport) {
        super(PacketType.UNIT_CREATED);
        this.unitSerializable = unitSerializable;
        this.statPurchasingPrice = statPurchasingPrice;
        this.inTransport = inTransport;
    }

    public UnitSerializable getUnitSerializable(){
        return unitSerializable;
    }
    public int getStatPurchasingPrice(){
        return statPurchasingPrice;
    }
    public boolean getInTransport(){
        return inTransport;
    }
}
