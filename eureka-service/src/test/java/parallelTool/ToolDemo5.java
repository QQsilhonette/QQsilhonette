package parallelTool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 3:32 下午
 * @description：ForkJoin
 */
public class ToolDemo5 extends RecursiveTask<Integer> {

    private int begin;
    private int end;

    public ToolDemo5(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        // 拆分任务
        if(end - begin <= 2) {
            // 计算
            for(int i = begin; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            // 拆分
            ToolDemo5 d1 = new ToolDemo5(begin, (begin + end) / 2);
            ToolDemo5 d2 = new ToolDemo5((begin + end) / 2 + 1, end);

            //执行任务
            d1.fork();
            d2.fork();

            Integer res1 = d1.join();
            Integer res2 = d2.join();

            sum = res1 + res2;
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        ForkJoinPool pool = new ForkJoinPool();
        Future<Integer> feature = pool.submit(new ToolDemo5(1, 100));
        System.out.println("...");
        System.out.println("计算的值为：" + feature.get());
    }
}
