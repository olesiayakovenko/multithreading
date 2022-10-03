package com.oyakovenko;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Outputs to the console a string consisting of numbers from 1 to n, but with some values replaced:
 * if the number is divisible by 3 - output fizz, if the number is divisible by 5 - output buzz, if
 * the number is divisible by 3 and 5 at the same time - output fizzbuzz
 */
public class PrintFizzBuzzNumber {

  private final BlockingQueue<String> outputQueue = new LinkedBlockingQueue<>();

  private final AtomicInteger currentNumber = new AtomicInteger(1);
  private final int n;

  public PrintFizzBuzzNumber(int n) {
    this.n = n;
  }

  /**
   * Checks if the number is divisible by 3, and if so, adds the String fizz to the output queue.
   */
  public void fizz() {
    while (currentNumber.get() <= n) {

      synchronized (outputQueue) {
        if (currentNumber.get() % 3 == 0 && currentNumber.get() % 5 != 0) {
          outputQueue.add("fizz");
          currentNumber.incrementAndGet();
          outputQueue.notifyAll();
        } else {
          try {
            outputQueue.wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  /**
   * Checks if the number is divisible by 5, and if so, adds the String buzz to the output queue.
   */
  public void buzz() {
    while (currentNumber.get() <= n) {

      synchronized (outputQueue) {
        if (currentNumber.get() % 3 != 0 && currentNumber.get() % 5 == 0) {
          outputQueue.add("buzz");
          currentNumber.incrementAndGet();
          outputQueue.notifyAll();
        } else {
          try {
            outputQueue.wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  /**
   * Checks if the number is divisible by 3 and 5 simultaneously, and if so, adds the String
   * fizzbuzz to the output queue.
   */
  public void fizzbuzz() {
    while (currentNumber.get() <= n) {

      synchronized (outputQueue) {
        if (currentNumber.get() % 3 == 0 && currentNumber.get() % 5 == 0) {
          outputQueue.add("fizzbuzz");
          currentNumber.incrementAndGet();
          outputQueue.notifyAll();
        } else {
          try {
            outputQueue.wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  /**
   * Checks if the number isn't divisible by 3 or 5, and if so, adds the number to the output queue
   * and outputs the data from the queue.
   */
  public void number() {
    while (currentNumber.get() <= n) {

      synchronized (outputQueue) {
        if (currentNumber.get() <= n && currentNumber.get() % 3 != 0
            && currentNumber.get() % 5 != 0) {
          outputQueue.add(String.valueOf(currentNumber.get()));
          currentNumber.incrementAndGet();
          outputQueue.notifyAll();
        } else {
          try {
            outputQueue.wait();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }

        while (!outputQueue.isEmpty()) {
          System.out.print(outputQueue.poll() + " ");
        }
      }
    }
  }

  public static void main(String[] args) {
    PrintFizzBuzzNumber printFizzBuzzNumber = new PrintFizzBuzzNumber(15);

    new Thread(printFizzBuzzNumber::fizz).start();
    new Thread(printFizzBuzzNumber::buzz).start();
    new Thread(printFizzBuzzNumber::fizzbuzz).start();
    new Thread(printFizzBuzzNumber::number).start();
  }
}