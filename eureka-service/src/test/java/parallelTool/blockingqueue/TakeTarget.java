package parallelTool.blockingqueue;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/23 2:55 下午
 * @description：
 */
public class TakeTarget implements Runnable {

    private Shop eShop;

    public TakeTarget(Shop eShop) {
        this.eShop = eShop;
    }

    @Override
    public void run() {
        while (true) {
            eShop.take();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
