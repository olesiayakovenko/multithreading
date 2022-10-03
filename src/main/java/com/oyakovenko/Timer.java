package com.oyakovenko;

/**
 * Every second displays on the screen data on the time elapsed since the start of the program,
 * every 5 seconds displays a message that 5 seconds have elapsed.
 */
public class Timer {

  public static void main(String[] args) throws InterruptedException {
    new Thread(() -> {
      for (int i = 1; ; i++) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        System.out.println(i);
      }
    }).start();

    while (true) {
      Thread.sleep(5000);
      System.out.println("Пройшло 5 секунд");
    }
  }
}