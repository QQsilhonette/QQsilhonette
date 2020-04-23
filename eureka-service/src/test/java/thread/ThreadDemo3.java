package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 4:11 下午
 * @description：实现Callable接口
 */
public class ThreadDemo3 implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("正在进行紧张的计算...");
        Thread.sleep(2000);
        return 1;
    }

    public static void main(String[] args) throws Exception {
        FutureTask<Integer> task = new FutureTask<>(new ThreadDemo3());
        Thread thread = new Thread(task);
        thread.start();
        Integer result = task.get();
        System.out.println("线程执行结果为：" + result);
    }
}
