package fxzone.net.server;

import java.net.Socket;

public class Server extends AbstractServer{

    @Override
    protected ServerProtocol createServerProtocol(Socket socket) {
        return new ServerProtocol(socket);
    }
}
