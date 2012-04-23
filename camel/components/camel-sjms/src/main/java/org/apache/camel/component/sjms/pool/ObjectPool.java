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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO Add Class documentation for ObjectPool
 * 
 */
public abstract class ObjectPool<T> {

    private static final int DEFAULT_POOL_SIZE = 1;
    private final BlockingQueue<T> objects;
    private int maxSize = DEFAULT_POOL_SIZE;
    private AtomicInteger poolCount = new AtomicInteger();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public ObjectPool() {
        this(DEFAULT_POOL_SIZE);
    }

    public ObjectPool(int poolSize) {
        this.maxSize = poolSize;
        this.objects = new ArrayBlockingQueue<T>(getMaxSize(), false);
    }
    
    public void destroyPool() throws Exception {
        getLock().writeLock().lock();
        try {
            while(poolCount.get() > 0) {
                destroyPoolObjects();
                poolCount.set(0);
            }
        } finally {
            getLock().writeLock().unlock();
        }
    }

    /**
     * Override to have new objects of type T created when the pool is
     * initialized empty.
     * 
     * @return
     * @throws Exception
     */
    protected abstract T createObject() throws Exception;

    /**
     * Clean up for pool objects
     * 
     * @return
     * @throws Exception
     */
    protected abstract void destroyPoolObjects() throws Exception;

    public T borrowObject() throws Exception {
        T t = null;
        getLock().writeLock().lock();
        try {
            if (poolCount.get() < getMaxSize()) {
                t = createObject();
                poolCount.incrementAndGet();
                objects.add(t);
            }
        } finally {
            getLock().writeLock().unlock();
        }
        return objects.poll(200, TimeUnit.MILLISECONDS);
    }

    public void returnObject(T object) throws Exception {
        this.objects.add(object);
    }

    List<T> drainObjectPool() {
        List<T> retList = new ArrayList<T>();
        getLock().writeLock().lock();
        try {
            this.objects.drainTo(retList);
        } finally {
            getLock().writeLock().unlock();
        }
        return retList;
    }
    
    int size() {
        return objects.size();
    }

    /**
     * Gets the ReadWriteLock value of lock for this instance of ObjectPool.
     *
     * @return the lock
     */
    protected ReadWriteLock getLock() {
        return lock;
    }

    /**
     * Gets the int value of maxSize for this instance of ObjectPool.
     * 
     * @return the maxSize
     */
    public int getMaxSize() {
        return maxSize;
    }
}
