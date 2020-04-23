package parallelTool;

import java.util.concurrent.Semaphore;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 3:32 下午
 * @description：Semaphore
 */
public class ToolDemo3 {

    public void method(Semaphore semaphore) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " is run...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
    }

    public static void main(String[] args) {
        ToolDemo3 toolDemo3 = new ToolDemo3();
        Semaphore semaphore = new Semaphore(10);

        for(int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    toolDemo3.method(semaphore);
                }
            }).start();
        }
    }
}
