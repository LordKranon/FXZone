package fxzone.controller;

import fxzone.game.logic.Player;
import java.util.ArrayList;

public interface ClientJoinedController extends NetworkController{

    void setLatestPlayerList(ArrayList<Player> players);
    void connectionClosed();
    void gameStart();
}
