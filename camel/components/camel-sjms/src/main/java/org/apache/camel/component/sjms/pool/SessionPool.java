/*
 * Copyright 2012 FuseSource
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.camel.component.sjms.pool;

import java.util.List;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.camel.component.sjms.jms.SessionAcknowledgementType;

/**
 * TODO Add Class documentation for SessionPool
 * 
 */
public class SessionPool extends ObjectPool<Session> {

    private ConnectionPool connectionPool;
    private boolean transacted = false;
    private SessionAcknowledgementType acknowledgeMode = SessionAcknowledgementType.AUTO_ACKNOWLEDGE;

    /**
     * TODO Add Constructor Javadoc
     *
     */
    public SessionPool(int poolSize, ConnectionPool connectionPool) {
        super(poolSize);
        this.connectionPool = connectionPool;
    }

    /**
     * TODO Add Constructor Javadoc
     *
     * @param poolSize
     */
    public SessionPool(int poolSize) {
        super(poolSize);
    }

    @Override
    protected Session createObject() throws Exception {
        Session session = null;
        final Connection connection = getConnectionPool().borrowObject();
        if(connection != null) {
            if(transacted) {
                session = connection.createSession(transacted, Session.SESSION_TRANSACTED);
            } else {
                switch (getAcknowledgeMode()) {
                case CLIENT_ACKNOWLEDGE:
                    session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
                    break;
                case DUPS_OK_ACKNOWLEDGE:
                    session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
                    break;
                case AUTO_ACKNOWLEDGE:
                    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                default:
                    break;
                }
            }
            getConnectionPool().returnObject(connection);
        }
        return session;
    }

    @Override
    protected void destroyPoolObjects() throws Exception {
        List<Session> list = drainObjectPool();
        for (Session session : list) {
            if(transacted)
                session.commit();
            session.close();
        }
    }

    /**
     * Gets the SessionAcknowledgementType value of acknowledgeMode for this instance of SessionPool.
     *
     * @return the DEFAULT_ACKNOWLEDGE_MODE
     */
    public final SessionAcknowledgementType getAcknowledgeMode() {
        return acknowledgeMode;
    }

    /**
     * Sets the SessionAcknowledgementType value of acknowledgeMode for this instance of SessionPool.
     *
     * @param acknowledgeMode Sets SessionAcknowledgementType, default is AUTO_ACKNOWLEDGE
     */
    public final void setAcknowledgeMode(SessionAcknowledgementType acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    /**
     * Gets the boolean value of transacted for this instance of SessionPool.
     *
     * @return the transacted
     */
    public final boolean isTransacted() {
        return transacted;
    }

    /**
     * Sets the boolean value of transacted for this instance of SessionPool.
     *
     * @param transacted Sets boolean, default is TODO add default
     */
    public final void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    /**
     * Gets the ConnectionPool value of connectionPool for this instance of SessionPool.
     *
     * @return the connectionPool
     */
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
