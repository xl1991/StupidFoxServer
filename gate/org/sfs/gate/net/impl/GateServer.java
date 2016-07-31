package org.sfs.gate.net.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.sfs.gate.net.IServer;
import org.sfs.gate.net.ISessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GateServer implements IServer {
    private static final Logger logger = LoggerFactory.getLogger(GateServer.class);
    private final int port;
    private final ISessionListener listener;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private volatile boolean isRunning;
    private Channel channel;

    public GateServer(int port, ISessionListener listener) {
        super();
        if (listener == null) {
            throw new IllegalArgumentException("Listener");
        }
        this.port = port;
        this.listener = listener;
    }

    @Override
    public synchronized void start() throws Exception {
        if (isRunning)
            throw new IllegalStateException("Already start");
        try {
            isRunning = true;
            bossGroup = new NioEventLoopGroup(1);
            workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.group(bossGroup, workGroup);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            bootstrap.childHandler(new GateChannelInitializer(this.listener));
            channel = bootstrap.bind(port).sync().channel();
        } catch (Exception e) {
            logger.error("Error on startup.", e);
            stop();
            throw e;
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public synchronized void stop() {
        isRunning = false;
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().syncUninterruptibly();
            bossGroup = null;
        }

        if (channel != null) {
            channel.close().syncUninterruptibly();
            channel = null;
        }

        if (workGroup != null) {
            workGroup.shutdownGracefully().syncUninterruptibly();
            workGroup = null;
        }
    }
}
