package thread;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/22 4:11 下午
 * @description：ThreadLocal
 */
public class ThreadDemo7 {

    private ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return new Integer(0);
        };
    };

    public int getNext() {
        Integer value = count.get();
        value++;
        count.set(value);
        return value;
    }

    public static void main(String[] args) {
        ThreadDemo7 threadDemo7 = new ThreadDemo7();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    System.out.println(Thread.currentThread().getName() + " " + threadDemo7.getNext());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    System.out.println(Thread.currentThread().getName() + " " + threadDemo7.getNext());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    System.out.println(Thread.currentThread().getName() + " " + threadDemo7.getNext());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
