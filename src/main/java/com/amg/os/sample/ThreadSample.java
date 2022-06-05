package sample;

public class ThreadSample {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    System.out.println("hi");
                }
            } catch (InterruptedException ex) {
                System.out.println("i was interrupted");
            }
        });

        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}
