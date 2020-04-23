package thread;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 4:11 下午
 * @description：join
 */
public class ThreadDemo6 {

    public void thread1(Thread joinThread) {
        System.out.println("线程1执行了...");
        joinThread.start();
        try {
            joinThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程1执行完毕");
    }

    public void thread2() {
        System.out.println("加塞线程开始执行...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("加塞线程执行完毕");
    }

    public static void main(String[] args) {
        ThreadDemo6 threadDemo6 = new ThreadDemo6();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                threadDemo6.thread2();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadDemo6.thread1(t1);
            }
        }).start();
    }
}
