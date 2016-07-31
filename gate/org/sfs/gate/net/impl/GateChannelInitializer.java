package org.sfs.gate.net.impl;

import org.sfs.gate.net.ISessionListener;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class GateChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ChannelHandler LENGTH_FIELD_PREPENDER = new LengthFieldPrepender(3);
    private final ChannelHandler MESSAGE_ENCODER = new MessageEncoder();
    private final ChannelHandler LOG_HANDLER = new LoggingHandler(LogLevel.INFO);
    private final ChannelHandler MESSAGE_HANDLER;

    public GateChannelInitializer(ISessionListener listener) {
        super();
        MESSAGE_HANDLER = new GateChannelEventHandler(listener);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 3, 0, 3));
        ch.pipeline().addLast(LENGTH_FIELD_PREPENDER);
        ch.pipeline().addLast(MESSAGE_ENCODER, new MessageDecoder());
        ch.pipeline().addLast(LOG_HANDLER);
        ch.pipeline().addLast(MESSAGE_HANDLER);
    }
}
