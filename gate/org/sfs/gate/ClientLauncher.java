package org.sfs.gate;

import org.sfs.gate.net.impl.GateClient;
import org.sfs.gate.net.impl.GateClientSessionListener;

public class ClientLauncher {
    public static void main(String[] args) throws Exception {
        GateClient client = new GateClient("127.0.0.1", 1024, new GateClientSessionListener());
        client.connect();
    }
}
