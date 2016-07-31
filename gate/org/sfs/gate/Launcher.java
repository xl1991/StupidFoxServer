package org.sfs.gate;

import org.sfs.gate.net.IServer;
import org.sfs.gate.net.impl.GateServer;
import org.sfs.gate.net.impl.GateServerSessionListener;

public class Launcher {

    public static void main(String[] args) throws Exception {
        IServer server = new GateServer(1024, new GateServerSessionListener());
        server.start();
    }
}
