package org.sfs.gate.net.impl;

import org.sfs.gate.net.IMessage;
import org.sfs.gate.net.ISession;
import org.sfs.gate.net.ISessionListener;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Sharable
public class GateChannelEventHandler extends ChannelInboundHandlerAdapter {
    private final AttributeKey<ISession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");
    private final ISessionListener listener;

    public GateChannelEventHandler(ISessionListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        listener.sessionOpen(createSession(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        listener.sessionClose(getSession(ctx));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMessage message = (IMessage) msg;
        ISession session = getSession(ctx);
        message.setSenderId(session.getSessionId().intValue());
        listener.sessionRead(session, message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        listener.sessionError(getSession(ctx), cause);
    }

    private ISession getSession(ChannelHandlerContext ctx) {
        Attribute<ISession> sessionAttr = ctx.attr(SESSION_KEY);
        return sessionAttr.get();
    }

    private ISession createSession(ChannelHandlerContext ctx) {
        final Attribute<ISession> sessionAttr = ctx.attr(SESSION_KEY);
        if (sessionAttr.get() != null) {
            throw new IllegalStateException("Session already created");
        }
        final ISession session = new GateSession(ctx.channel());
        sessionAttr.set(session);
        return session;
    }
}
