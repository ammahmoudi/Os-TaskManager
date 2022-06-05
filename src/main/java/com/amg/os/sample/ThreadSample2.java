package sample;

public class ThreadSample2 {
    
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("hi");
                } catch (InterruptedException ex) {
                    System.out.println("i was interrupted");
                }
            }
        });

        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}
