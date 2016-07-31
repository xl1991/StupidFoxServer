package org.sfs.gate.net.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;
import org.sfs.gate.net.IMessage;

@Sharable
public class MessageEncoder extends MessageToByteEncoder<IMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IMessage msg, ByteBuf out) throws Exception {
        msg.writeTo(out);
    }
}
