package org.sfs.gate.net.impl;

import org.sfs.gate.net.IMessage;
import org.sfs.gate.net.ISession;
import org.sfs.gate.net.ISessionListener;
import org.sfs.gate.net.msg.Message;

public class GateClientSessionListener implements ISessionListener {

    @Override
    public void sessionOpen(ISession session) {
        // Send to myself for test purpose
        Message message = new Message();
        message.setCmdKey(1);
        message.setReceiverId(10001);
        message.setSenderId(10001);
        message.setBody(Message.EMPTY_BYTE_ARRAY);
        session.send(message);
    }

    @Override
    public void sessionClose(ISession session) {
    }

    @Override
    public void sessionRead(ISession session, IMessage message) {
        message.setReceiverId(10001);
        message.setSenderId(10001);
        session.send(message);
    }

    @Override
    public void sessionError(ISession session, Throwable e) {
    }

}
