package org.sfs.gate.net;

import java.util.List;

public interface ISession {
    long getCreateTime();

    Integer getSessionId();

    void send(IMessage message);

    void send(List<IMessage> messages);

    <T> T getProperty(String key);

    <T> T setProperty(String key, T value);

    <T> T removeProperty(String key);

    boolean isOpen();

    void close();
}
