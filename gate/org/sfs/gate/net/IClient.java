package org.sfs.gate.net;

public interface IClient {
    void connect() throws Exception;

    boolean isConnected();

    void disconnect();

    void send(IMessage message);
}
