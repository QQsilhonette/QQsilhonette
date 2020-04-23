package parallelTool;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 3:32 下午
 * @description：CyclicBarrier，与CountDownLatch区别是采用加计数，await()使计数+1，可以重置
 */
public class ToolDemo2 {

    Random random = new Random();
    public void meeting(CyclicBarrier barrier) {
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "到达会议室，等待会议开始...");
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ToolDemo2 toolDemo2 = new ToolDemo2();

        CyclicBarrier barrier = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println("开始开会...");
            }
        });

        for(int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    toolDemo2.meeting(barrier);
                }
            }).start();
        }
    }
}
