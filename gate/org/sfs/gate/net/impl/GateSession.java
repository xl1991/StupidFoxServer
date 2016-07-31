package org.sfs.gate.net.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;

import org.sfs.gate.net.IMessage;
import org.sfs.gate.net.ISession;

public class GateSession implements ISession {
    private static final AtomicInteger Id = new AtomicInteger(10000);
    private final Integer id;
    private final long createTime;
    private final Channel channel;
    private final Map<String, Object> attrsMap;

    public GateSession(Channel channel) {
        super();
        this.channel = channel;
        id = Id.incrementAndGet();
        createTime = System.currentTimeMillis();
        attrsMap = new ConcurrentHashMap<String, Object>();
    }

    @Override
    public void send(IMessage message) {
        channel.writeAndFlush(message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(String key) {
        return (T) attrsMap.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T setProperty(String key, T value) {
        return (T) attrsMap.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T removeProperty(String key) {
        return (T) attrsMap.remove(key);
    }

    @Override
    public boolean isOpen() {
        return channel.isActive();
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public Integer getSessionId() {
        return id;
    }

    @Override
    public void send(List<IMessage> messages) {
        for (IMessage message : messages) {
            channel.write(message);
        }
        channel.flush();
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }
}
