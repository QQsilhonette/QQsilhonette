package thread;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 3:32 下午
 * @description：线程初始化、中断
 */
public class ThreadDemo1 extends Thread {

    public ThreadDemo1(String name) {
        super(name);
    }

    @Override
    public void run() {
        // interrupted（）是检测中断并清除中断状态；isInterrupted（）只检测中断
        while(!interrupted()) {
            System.out.println(getName() + "线程执行了...");
            try {
                Thread.sleep(2000);
            } catch(InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        ThreadDemo1 d1 = new ThreadDemo1("first-thread");
        ThreadDemo1 d2 = new ThreadDemo1("second-thread");

        // setDaemon(true)设置线程为守护线程，如果没有其他用户线程则结束
//        d1.setDaemon(true);
//        d2.setDaemon(true);

        d1.start();
        d2.start();

        // interrupt()不会真正终止线程
        d1.interrupt();
    }

}
