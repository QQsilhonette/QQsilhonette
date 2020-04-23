import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class test {
    private int outPutNum = 1;
    boolean t1_run_flag = true;
    @Test
    public void main() {
        Lock lock = new ReentrantLock();
        Condition condThread1 = lock.newCondition();
        Condition condThread2 = lock.newCondition();
        Thread thread_1 = new Thread() {
            public void run() {
                try {
                    lock.lock();
                    while (outPutNum <= 100) {
                        if (!t1_run_flag) {
                            condThread1.await();
                        }
                        if(outPutNum > 100) {
                            break;
                        }
                        System.out.println("thread_1:" + outPutNum);
                        outPutNum++;
                        t1_run_flag = false;
                        condThread2.signal();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread thread_2 = new Thread() {
            public void run() {
                try {
                    lock.lock();
                    while (outPutNum <= 100) {
                        if (t1_run_flag) {
                            condThread2.await();
                        }
                        if(outPutNum > 100) {
                            break;
                        }
                        System.out.println("thread_2:" + outPutNum);
                        outPutNum++;
                        t1_run_flag = true;
                        condThread1.signal();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        thread_1.start();
        thread_2.start();
    }
}