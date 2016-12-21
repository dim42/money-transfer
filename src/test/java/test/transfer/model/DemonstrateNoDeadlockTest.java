package test.transfer.model;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DemonstrateNoDeadlockTest {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;

    public static void main(String[] args) {
        final Random rnd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account("num" + rnd.nextInt(100));
            accounts[i].balance = new BigDecimal(rnd.nextInt(1000) + 200);
        }
        class TransferThread extends Thread {
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromInd = rnd.nextInt(NUM_ACCOUNTS);
                    int toInd = rnd.nextInt(NUM_ACCOUNTS);
                    Account fromAcct = accounts[fromInd];
                    Account toAcct = accounts[toInd];
                    while (fromAcct.number.equals(toAcct.number)) {
                        toInd = rnd.nextInt(NUM_ACCOUNTS);
                        toAcct = accounts[toInd];
                    }
                    BigDecimal amount = new BigDecimal(rnd.nextInt(100));
                    try {
//                        fromAcct.transferMoney(toAcct, amount);
//                        fromAcct.transferMoney1(toAcct, amount);
//                        fromAcct.transferMoney2(toAcct, amount);
//                        new Transfer(fromAcct, toAcct).transferMoney(amount);
                        new Transfer(null, null).transferMoney(fromAcct, toAcct, amount);
                    } catch (RuntimeException ignored) {
                    }
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(in1);
//        System.out.println(in2);
        System.out.println(Transfer.in1);
        System.out.println(Transfer.in2);
    }
}
