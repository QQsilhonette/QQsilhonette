package thread;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 4:11 下午
 * @description：定时器
 */
public class ThreadDemo4 {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("timertask is run");
            }
        }, 0, 1000);
    }
}
