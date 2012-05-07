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

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.XAConnection;
import javax.jms.XASession;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import org.apache.camel.component.sjms.jms.SessionAcknowledgementType;

/**
 * TODO Add Class documentation for SessionPool
 * 
 */
public class SessionPool extends ObjectPool<Session> {

    private ConnectionPool connectionPool;
    private boolean transacted = false;
    private SessionAcknowledgementType acknowledgeMode = SessionAcknowledgementType.AUTO_ACKNOWLEDGE;
    private TransactionManager transactionManager;

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
     */
    public SessionPool(int poolSize, ConnectionPool connectionPool, TransactionManager transactionManager) {
        super(poolSize);
        this.connectionPool = connectionPool;
        this.transactionManager = transactionManager;
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
            if (isXa()) {
                try {
                    XAConnection xaconn = (XAConnection) connection;
                    transacted = true;
                    acknowledgeMode = SessionAcknowledgementType.SESSION_TRANSACTED;
                    session = (XASession) xaconn.createXASession();
//                        session.setIgnoreClose(true);
//                        setIsXa(true);
//                        transactionManager.getTransaction().registerSynchronization(new Synchronization((XASession) session));
//                        incrementReferenceCount();
                    transactionManager.getTransaction().enlistResource(createXaResource((XASession) session));
                } catch (RollbackException e) {
                    final JMSException jmsException = new JMSException("Rollback Exception");
                    jmsException.initCause(e);
                    throw jmsException;
                } catch (SystemException e) {
                    final JMSException jmsException = new JMSException("System Exception");
                    jmsException.initCause(e);
                    throw jmsException;
                }
            } else if (isLocalTransaction()) {
                session = connection.createSession(transacted, acknowledgeMode.intValue());
            } else {
                switch (acknowledgeMode) {
                case CLIENT_ACKNOWLEDGE:
                    session = connection.createSession(transacted, Session.CLIENT_ACKNOWLEDGE);
                    break;
                case DUPS_OK_ACKNOWLEDGE:
                    session = connection.createSession(transacted, Session.DUPS_OK_ACKNOWLEDGE);
                    break;
                case AUTO_ACKNOWLEDGE:
                    session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
                default:
                    break;
                }
            }
        }
        getConnectionPool().returnObject(connection);
        return session;
    }

    private boolean isXa() throws Exception {
        return (transactionManager != null && transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION);
    }

    private boolean isLocalTransaction() {
        return (transactionManager == null && this.transacted);
    }
    
    @Override
    protected void destroyObject(Session session) throws Exception {
     // lets reset the session
        session.setMessageListener(null);

        if (transacted && !isXa()) {
            try {
                session.rollback();
            } catch (JMSException e) {
                logger.warn("Caught exception trying rollback() when putting session back into the pool, will invalidate. " + e, e);
            }
        }
        if (session != null) {
            session.close();
            session = null;
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

    protected XAResource createXaResource(XASession session) throws JMSException {
        return session.getXAResource();
    }
    
    
//    protected class Synchronization implements javax.transaction.Synchronization {
//        private final XASession session;
//
//        private Synchronization(XASession session) {
//            this.session = session;
//        }
//
//        public void beforeCompletion() {
//        }
//        
//        public void afterCompletion(int status) {
//            try {
//                // This will return session to the pool.
//                session.setIgnoreClose(false);
//                session.close();
//                session.setIsXa(false);
//            } catch (JMSException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
