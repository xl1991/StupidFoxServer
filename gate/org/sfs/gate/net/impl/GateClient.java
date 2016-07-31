package org.sfs.gate.net.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.sfs.gate.net.IClient;
import org.sfs.gate.net.IMessage;
import org.sfs.gate.net.ISessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GateClient implements IClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String host;
    private final int port;
    private final ISessionListener listener;
    private EventLoopGroup group;
    private Channel channel;
    private boolean isConnected;

    public GateClient(String host, int port, ISessionListener listener) {
        super();
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        this.host = host;
        this.port = port;
        this.listener = listener;
    }

    @Override
    public synchronized void connect() throws Exception {
        if (isConnected)
            throw new IllegalStateException("Already connected");
        try {
            group = new NioEventLoopGroup(1);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new GateChannelInitializer(listener));
            channel = bootstrap.connect(host, port).sync().channel();
            isConnected = true;
        } catch (Exception e) {
            logger.error("Connect faild.", e);
            disconnect();
        }
    }

    @Override
    public synchronized boolean isConnected() {
        return isConnected;
    }

    @Override
    public synchronized void disconnect() {
        isConnected = false;
        if (channel != null) {
            channel.close().syncUninterruptibly();
            channel = null;
        }
        if (group != null) {
            group.shutdownGracefully().syncUninterruptibly();
            group = null;
        }
    }

    @Override
    public synchronized void send(IMessage message) {
        if (!isConnected()) {
            throw new IllegalStateException("Disconnected");
        }
        channel.writeAndFlush(message);
    }

}
