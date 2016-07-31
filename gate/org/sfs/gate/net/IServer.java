package org.sfs.gate.net;

public interface IServer {
    void start() throws Exception;

    boolean isRunning();

    void stop() throws Exception;
}
