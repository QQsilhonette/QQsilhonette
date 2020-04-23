package parallelTool;

import java.util.concurrent.locks.StampedLock;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/23 6:47 下午
 * @description：StampedLock
 */
public class ToolDemo7 {

    private int balance;
    private StampedLock lock = new StampedLock();

    public void conditionReadWrite(int value) {
        // 首先判断balance是否满足条件
        long stamp = lock.readLock();
        while(balance > 0) {
            long writeStamp = lock.tryConvertToWriteLock(stamp);
            // 成功转换成写锁
            if(writeStamp != 0) {
                stamp = writeStamp;
                balance += value;
                break;
            } else {
                // 没有转换成写锁，这里需要首先释放读锁，然后在拿到写锁
                lock.unlockRead(stamp);
                // 获取写锁
                stamp = lock.writeLock();
            }
        }
        lock.unlock(stamp);
    }

    public void optimisticRead() {
        long stamp = lock.tryOptimisticRead();
        int res = balance;
        // 这里可能会出现写操作，因此要进行判断
        if(!lock.validate(stamp)) {
            // 重新获取读锁
            long readStamp = lock.readLock();
            res = balance;
            stamp = readStamp;
        }
        lock.unlockRead(stamp);
    }

    public void read() {
        long stamp = lock.readLock();
        int res = balance;
        // 释放读锁
        lock.unlockRead(stamp);
    }

    public void write(int value) {
        long stamp = lock.writeLock();
        balance += value;
        lock.unlockWrite(stamp);
    }
}
