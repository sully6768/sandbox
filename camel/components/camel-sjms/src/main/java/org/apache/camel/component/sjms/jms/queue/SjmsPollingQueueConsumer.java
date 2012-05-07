package org.apache.camel.component.sjms.jms.queue;

import java.util.concurrent.ScheduledExecutorService;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.sjms.SimpleJmsEndpoint;
import org.apache.camel.component.sjms.pool.SessionPool;
import org.apache.camel.impl.ScheduledPollConsumer;

public class SjmsPollingQueueConsumer extends ScheduledPollConsumer {

    private SessionPool sessions;
    /**
     * TODO Add Constructor Javadoc
     *
     * @param endpoint
     * @param processor
     * @param executor
     */
    public SjmsPollingQueueConsumer(Endpoint endpoint, Processor processor,
            ScheduledExecutorService executor) {
        super(endpoint, processor, executor);
        this.sessions = ((SimpleJmsEndpoint) endpoint).getSessions();
    }


    /**
     * TODO Add Constructor Javadoc
     *
     * @param endpoint
     * @param processor
     */
    public SjmsPollingQueueConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }


    @Override
    protected int poll() throws Exception {
        final String destinationName = ((SimpleJmsEndpoint) getEndpoint()).getDestinationName();
        final String messageSelector = null; 
        QueueSession queueSession = (QueueSession) sessions.borrowObject();
        Queue myQueue = queueSession.createQueue(destinationName);
        QueueReceiver messageConsumer = null;
        if (messageSelector == null || messageSelector.equals(""))
            messageConsumer = queueSession.createReceiver(myQueue);
        else
            messageConsumer = queueSession.createReceiver(myQueue,
                    messageSelector);
        Message mesg = messageConsumer.receive();
        sessions.returnObject(queueSession);
        
        Exchange exchange = getEndpoint().createExchange();

        exchange.getIn().setBody(mesg);

        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            return 1; // number of messages polled
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}
