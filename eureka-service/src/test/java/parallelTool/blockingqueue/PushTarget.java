package parallelTool.blockingqueue;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/23 2:55 下午
 * @description：
 */
public class PushTarget implements Runnable {

    private Shop eShop;

    public PushTarget(Shop eShop) {
        this.eShop = eShop;
    }

    @Override
    public void run() {
        while (true) {
            eShop.push();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
