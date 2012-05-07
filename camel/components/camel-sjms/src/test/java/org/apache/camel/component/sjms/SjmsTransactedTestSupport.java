package org.apache.camel.component.sjms;

import javax.transaction.TransactionManager;

import com.atomikos.icatch.jta.UserTransactionManager;

public abstract class SjmsTransactedTestSupport extends
        SjmsConnectionTestSupport {

    private TransactionManager transactionManager;

    @Override
    public void setup() throws Exception {
        super.setup();
        if (transactionManager == null) {
            createTransactionManager();
        }
    }
    
    @Override
    public void teardown() throws Exception {
        if (transactionManager == null) {
            ((UserTransactionManager) transactionManager).close();
            transactionManager = null;
        }
        super.teardown();
    }

    /**
     * Sets the TransactionManager value of transactionManager for this instance
     * of SjmsConnectionTestSupport.
     * 
     * @param transactionManager
     *            Sets TransactionManager, default is TODO add default
     */
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Gets the TransactionManager value of transactionManager for this instance
     * of SjmsConnectionTestSupport.
     * 
     * @return the transactionManager
     */
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Gets the TransactionManager value of transactionManager for this instance
     * of SjmsConnectionTestSupport.
     * 
     * @return the transactionManager
     * @throws Exception 
     */
    protected void createTransactionManager() throws Exception {
        transactionManager = new UserTransactionManager();
        ((UserTransactionManager) transactionManager).setForceShutdown(false);
        ((UserTransactionManager) transactionManager).setTransactionTimeout(300);
        ((UserTransactionManager) transactionManager).init();

    }

}
