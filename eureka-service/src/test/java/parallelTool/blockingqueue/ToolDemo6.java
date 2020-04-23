package parallelTool.blockingqueue;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 3:32 下午
 * @description：ForkJoin
 */
public class ToolDemo6 {

    public static void main(String[] args) {
        Shop eShop = new EShop();

        TakeTarget takeTarget = new TakeTarget(eShop);
        PushTarget pushTarget = new PushTarget(eShop);

        new Thread(pushTarget).start();
        new Thread(pushTarget).start();
        new Thread(pushTarget).start();
        new Thread(pushTarget).start();
        new Thread(pushTarget).start();

        new Thread(takeTarget).start();
        new Thread(takeTarget).start();
        new Thread(takeTarget).start();
        new Thread(takeTarget).start();
        new Thread(takeTarget).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                eShop.size();
            }
        }).start();
    }
}
