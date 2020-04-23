package parallelTool;

import java.util.concurrent.Exchanger;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 3:32 下午
 * @description：Exchanger
 */
public class ToolDemo4 {

    public void exchangeA(Exchanger<String> exch) {
        System.out.println("exchangeA 方法执行...");

        try {
            System.out.println("exchangeA 线程正在抓取数据...");
            Thread.sleep(2000);
            System.out.println("exchangeA 线程抓取数据结束...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String res = "12345";
        try {
            System.out.println("等待对比结果...");
            exch.exchange(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exchangeB(Exchanger<String> exch) {
        System.out.println("exchangeB 方法执行...");

        try {
            System.out.println("exchangeB 线程正在抓取数据...");
            Thread.sleep(2000);
            System.out.println("exchangeB 线程抓取数据结束...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String res = "12345";
        try {
            String value = exch.exchange(res);
            System.out.println("开始进行比对...");
            System.out.println("对比结果: " + value.equals(res));
            exch.exchange(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ToolDemo4 toolDemo4 = new ToolDemo4();
        Exchanger<String> exch = new Exchanger<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                toolDemo4.exchangeA(exch);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                toolDemo4.exchangeB(exch);
            }
        }).start();
    }
}
