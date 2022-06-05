package com.amg.os.test;

import java.util.Scanner;

public class InterrruptScannerTest {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        Thread thread = new Thread(() -> {
            scanner.nextLine();
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        Thread.yield();

        scanner.close();
    }
}
