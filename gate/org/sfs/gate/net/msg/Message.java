package org.sfs.gate.net.msg;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

import org.sfs.gate.net.IMessage;

public class Message implements IMessage {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private int senderId;
    private int receiverId;
    private int cmdKey;
    private byte[] body;

    @Override
    public void writeTo(ByteBuf buf) {
        buf.ensureWritable(8 + body.length);
        buf.writeInt(senderId);
        buf.writeInt(cmdKey);
        buf.writeBytes(body);
    }

    @Override
    public int getReceiverId() {
        return receiverId;
    }

    @Override
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public int getCmdKey() {
        return cmdKey;
    }

    public void setCmdKey(int cmdKey) {
        this.cmdKey = cmdKey;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }

    @Override
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public static Message convertTo(ByteBuf buf) {
        Message message = new Message();
        message.setReceiverId(buf.readInt());
        message.setCmdKey(buf.readInt());
        final int readablebytes = buf.readableBytes();
        if (readablebytes == 0) {
            message.setBody(EMPTY_BYTE_ARRAY);
        } else {
            final byte[] body = new byte[readablebytes];
            buf.readBytes(body);
            message.setBody(body);
        }
        return message;
    }

    @Override
    public String toString() {
        return "Message [senderId=" + senderId + ", receiverId=" + receiverId + ", cmdKey=" + cmdKey + ", body="
                + Arrays.toString(body) + "]";
    }
}
