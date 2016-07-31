package org.sfs.gate.net;

import io.netty.buffer.ByteBuf;

public interface IMessage {
    void writeTo(ByteBuf buf);

    int getReceiverId();

    int getCmdKey();

    int getSenderId();

    void setReceiverId(int receiverId);

    void setSenderId(int senderId);
}
