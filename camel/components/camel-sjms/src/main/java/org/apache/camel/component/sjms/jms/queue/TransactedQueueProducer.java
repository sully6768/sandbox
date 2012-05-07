package org.apache.camel.component.sjms.jms.queue;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.apache.camel.Exchange;
import org.apache.camel.component.sjms.SimpleJmsEndpoint;
import org.apache.camel.component.sjms.SimpleJmsProducer;
import org.apache.camel.component.sjms.pool.SessionPool;

public class TransactedQueueProducer extends SimpleJmsProducer {

    private String destinationName = null;
    private SessionPool sessions;

    public TransactedQueueProducer(SimpleJmsEndpoint endpoint) {
        super(endpoint);
        this.destinationName = endpoint.getDestinationName();
        this.sessions = endpoint.getSessions();
    }

    public void process(Exchange exchange) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.info("Message to Produce: " + exchange.getIn().getBody());
        }

        QueueSession queueSession = (QueueSession) sessions.borrowObject();
        Queue myQueue = queueSession.createQueue(this.destinationName);
        QueueSender queueSender = queueSession.createSender(myQueue);
        
        try {
            TextMessage textMessage = queueSession.createTextMessage();
            textMessage.setText((String) exchange.getIn().getBody());
            queueSender.send(textMessage);
            queueSession.commit();
        } catch (JMSException e) {
            logger.error("Exception committing message. " + "Error Code: " + e.getErrorCode() + " - " + e.getLocalizedMessage());
            if (queueSession != null)
                queueSession.rollback();
        } finally {
            queueSender.close();
            sessions.returnObject(queueSession);        
        }
        
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }
}
