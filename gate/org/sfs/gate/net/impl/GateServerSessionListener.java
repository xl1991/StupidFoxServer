package org.sfs.gate.net.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sfs.gate.net.IMessage;
import org.sfs.gate.net.ISession;
import org.sfs.gate.net.ISessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GateServerSessionListener implements ISessionListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Integer, ISession> sessions = new ConcurrentHashMap<Integer, ISession>(1024);

    @Override
    public void sessionOpen(ISession session) {
        sessions.put(session.getSessionId(), session);
    }

    @Override
    public void sessionClose(ISession session) {
        sessions.remove(session.getSessionId());
    }

    @Override
    public void sessionRead(ISession session, IMessage message) {
        ISession receiverSession = sessions.get(message.getReceiverId());
        if (receiverSession != null) {
            receiverSession.send(message);
        }
    }

    @Override
    public void sessionError(ISession session, Throwable e) {
        logger.error("Session[{}] error.", session.getSessionId(), e);
        session.close();
    }
}
