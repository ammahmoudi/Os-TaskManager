package sample;

public class ThreadSample3 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
            () -> {
                while (!Thread.interrupted()) {
                    // do nothing
                }
                System.out.println("i was interrupted");
            }
        );

        thread.start();

        Thread.sleep(2000);
        thread.interrupt();
    }
}
