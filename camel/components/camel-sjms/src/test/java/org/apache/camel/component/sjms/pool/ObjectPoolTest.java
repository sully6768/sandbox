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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Add Class documentation for ObjectPoolTest
 * 
 */
public class ObjectPoolTest {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ObjectPoolTest.class);
    

    private AtomicInteger atomicInteger;
    
    @Before
    public void setUp() {
        atomicInteger = new AtomicInteger();
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#ObjectPool()}.
     * 
     * @throws Exception
     */
    @Test
    public void testObjectPool() throws Exception {
        TestPool testPool = new TestPool();
        assertNotNull(testPool);
        MyPooledObject pooledObject = testPool.borrowObject();
        assertNotNull(pooledObject);
        assertTrue("Expected a value of 1.  Returned: " + pooledObject.getObjectId(), pooledObject.getObjectId() == 1);
        
        MyPooledObject nextPooledObject = testPool.borrowObject();
        assertNull(nextPooledObject);
        
        testPool.returnObject(pooledObject);
        nextPooledObject = testPool.borrowObject();
        assertNotNull(nextPooledObject);
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#ObjectPool()}.
     */
    @Test
    public void testBadObjectPool() {
        try {
            new BadTestPool();
        } catch (Exception e) {
            assertTrue("Should have thrown an IllegalStateException", e instanceof IllegalStateException);
        }
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#ObjectPool(int)}.
     * 
     * @throws Exception
     */
    @Test
    public void testObjectPoolInt() throws Exception {
        final int maxPoolObjects = 5;
        TestPool testPool = new TestPool(maxPoolObjects);
        List<MyPooledObject> poolObjects = new ArrayList<MyPooledObject>();
        for (int i = 0; i < maxPoolObjects; i++) {
            poolObjects.add(testPool.borrowObject());
        }
        for (int i = 0; i < maxPoolObjects; i++) {
            MyPooledObject pooledObject = poolObjects.get(i);
            assertNotNull("MyPooledObject was null for borrow attempt: " + i, pooledObject);
            assertTrue("Expected a value in the range of 1-5.  Returned: " + pooledObject.getObjectId(), pooledObject.getObjectId() > 0 && pooledObject.getObjectId() < 6);
            LOGGER.info("MyPooledObject has an ID of: " + pooledObject.getObjectId());
        }
        
        assertNull("Pool should be empty", testPool.borrowObject());
        
        for (MyPooledObject myPooledObject : poolObjects) {
            testPool.returnObject(myPooledObject);
        }
        
        MyPooledObject pooledObject = testPool.borrowObject();
        assertNotNull(pooledObject);
        assertTrue("Expected a value in the range of 1-5.  Returned: " + pooledObject.getObjectId(), pooledObject.getObjectId() > 0 && pooledObject.getObjectId() < 6);

    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#createObject()}.
     * 
     * @throws Exception
     */
    @Test
    public void testCreateObject() throws Exception {
        TestPool testPool = new TestPool();
        assertNotNull(testPool.createObject());
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#borrowObject()}.
     * 
     * @throws Exception 
     */
    @Test
    public void testBorrowObject() throws Exception {
        TestPool testPool = new TestPool();
        MyPooledObject pooledObject = testPool.borrowObject();
        assertNotNull(pooledObject);
        assertTrue("Expected a value of 1.  Returned: " + pooledObject.getObjectId(), pooledObject.getObjectId() == 1);

        MyPooledObject nextPooledObject = testPool.borrowObject();
        assertNull("Expected a null as the pool of 1 was already removed", nextPooledObject);
    }

    /**
     * Test method for
     * {@link org.apache.camel.component.sjms.pool.ObjectPool#returnObject(java.lang.Object)}
     * .
     * @throws Exception 
     */
    @Test
    public void testReturnObject() throws Exception {
        TestPool testPool = new TestPool();
        assertNotNull(testPool);
        MyPooledObject pooledObject = testPool.borrowObject();
        MyPooledObject nextPooledObject = testPool.borrowObject();
        testPool.returnObject(pooledObject);
        nextPooledObject = testPool.borrowObject();
        assertNotNull(nextPooledObject);
    }

    class TestPool extends ObjectPool<MyPooledObject> {
        
        public TestPool() {
            super();
        }

        public TestPool(int poolSize) {
            super(poolSize);
        }

        @Override
        protected MyPooledObject createObject() throws Exception {
            return new MyPooledObject(atomicInteger.incrementAndGet());
        }

        @Override
        protected void destroyPoolObjects() throws Exception {
            // TODO Auto-generated method stub
            
        }

    }
    
    class MyPooledObject {
        private int objectId = -1;

        public MyPooledObject(int objectId) {
            this.objectId = objectId;
        }

        /**
         * 
         * @return the OBJECT_ID
         */
        public Integer getObjectId() {
            return this.objectId;
        }
    }
    

    class BadTestPool extends ObjectPool<Object> {
        @Override
        protected Object createObject() throws Exception {
            throw new Exception();
        }

        @Override
        protected void destroyPoolObjects() throws Exception {
            throw new Exception();
        }
    }
}
