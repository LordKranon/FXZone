package fxzone.controller;

import fxzone.game.logic.Player;
import fxzone.game.logic.serializable.GameSerializable;
import fxzone.net.packet.GameActionPacket;
import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;

public interface ClientJoinedController extends NetworkController{

    void setLatestPlayerList(ArrayList<Player> players);
    void connectionClosed();
    void gameStart(GameSerializable gameSerializable, String playerName);
    void gameActionReceived(GameActionPacket gameActionPacket);
}
