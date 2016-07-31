package org.sfs.gate.net;

public interface ISessionListener {
    void sessionOpen(ISession session);

    void sessionClose(ISession session);

    void sessionRead(ISession session, IMessage message);

    void sessionError(ISession session, Throwable e);

    void sessionIdle(ISession session);
}
