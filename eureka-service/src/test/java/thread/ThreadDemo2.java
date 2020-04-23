package thread;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 4:11 下午
 * @description：实现Runnable接口
 */
public class ThreadDemo2 implements Runnable {

    @Override
    public void run() {
        while (true) {
            System.out.println("thread running...");
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new ThreadDemo2());
        thread.start();
    }
}
