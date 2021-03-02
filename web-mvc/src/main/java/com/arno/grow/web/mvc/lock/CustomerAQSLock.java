package com.arno.grow.web.mvc.lock;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/2 7:11 下午
 * @version:
 */
public class CustomerAQSLock {

    private static final int UNLOCK = 0;
    private static final int LOCK = 1;

    /**
     * 定义一个可重入的同步器，继承 AQS
     */
    private static class CustomerSync extends AbstractQueuedSynchronizer implements Serializable {

        private static final long serialVersionUID = -5044951902480232606L;

        /**
         * 覆盖父类的 tryAcquire 获取锁方法，尝试获取锁
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            // 获取当前锁状态
            int stateTemp = getState();
            // 判断是否为线程第一次获取锁
            if (stateTemp == UNLOCK) {
                // 尝试加锁
                if (compareAndSetState(UNLOCK, arg)) {
                    // 如果加锁成功, 则设置当前线程独占访问
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            }
            // 如果当前线程和独占访问的线程是否一致，此操作是保证可重入
            else if (Thread.currentThread() == getExclusiveOwnerThread()) {
                // 则加锁状态变成持有锁的次数
                int newState = stateTemp + arg;
                // 检查是否次数已经溢出
                if (newState < 0) {
                    throw new RuntimeException("int 溢出");
                }
                // 设置状态
                setState(newState);
                return true;
            }
            return false;
        }

        /**
         * 覆盖父类的 tryRelease 方法，尝试释放锁
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            // 检查释放锁的线程是否和获取锁的线程一致
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new RuntimeException("独占访问线程和当前线程不一致");
            }
            int subVal = getState() - arg;
            // 记录是否已经将锁释放完毕
            boolean hasFreeLock = false;
            // 如果已经释放完锁
            if (subVal == UNLOCK) {
                hasFreeLock = true;
                // 清除独占访问的线程
                setExclusiveOwnerThread(null);
            }
            // 更新状态锁
            setState(subVal);
            return hasFreeLock;
        }
    }

    private final CustomerSync sync = new CustomerSync();

    /**
     * 加锁
     */
    public void lock() {
        sync.acquire(LOCK);
    }

    /**
     * 解锁
     */
    public void unlock() {
        sync.release(LOCK);
    }

}
