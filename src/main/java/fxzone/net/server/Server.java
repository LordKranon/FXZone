package fxzone.net.server;

import java.net.Socket;

public class Server extends AbstractServer{

    @Override
    protected ServerProtocol createServerProtocol(Socket socket) {
        System.out.println("[SERVER] Creating ServerProtocol");
        return new ServerProtocol(socket);
    }
}
