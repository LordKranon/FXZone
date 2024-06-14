package fxzone.net.packet;

import fxzone.game.logic.serializable.UnitSerializable;

public class UnitCreatedPacket extends GameActionPacket{

    private final UnitSerializable unitSerializable;

    private final int statPurchasingPrice;

    public UnitCreatedPacket(UnitSerializable unitSerializable, int statPurchasingPrice) {
        super(PacketType.UNIT_CREATED);
        this.unitSerializable = unitSerializable;
        this.statPurchasingPrice = statPurchasingPrice;
    }

    public UnitSerializable getUnitSerializable(){
        return unitSerializable;
    }
    public int getStatPurchasingPrice(){
        return statPurchasingPrice;
    }
}
