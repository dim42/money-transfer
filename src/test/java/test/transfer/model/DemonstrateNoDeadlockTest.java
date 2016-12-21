package test.transfer.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.transfer.api.AccountDao;
import test.transfer.service.Transfer;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class DemonstrateNoDeadlockTest {
    private static final Logger log = LogManager.getLogger();
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;
    //    private static final int NUM_ITERATIONS = 1;
    private static final Random rnd = new Random();
    private static AccountDao accountDao = new AccountDao() {
        @Override
        public Account findAccount(String accNum) {
            return new Account(accNum, String.valueOf(rnd.nextInt(1000) + 200000000), null, null, true, String.valueOf(rnd.nextInt(1000) + 300));
        }

        @Override
        public void updateAccountsBalance(String accNum1, String balance1, String accNum2, String balance2) {
        }
    };

    public static void main(String[] args) {
        final Account[] accounts = new Account[NUM_ACCOUNTS];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account("num" + i, String.valueOf(rnd.nextInt(1000) + 200000000), null, null, true, String.valueOf(rnd.nextInt
                    (1000) + 300));
//            accounts[i].balance = new BigDecimal(rnd.nextInt(1000) + 200);
//            accounts[i].balance(new BigDecimal(rnd.nextInt(1000) + 200));
        }
        final CountDownLatch latch = new CountDownLatch(NUM_THREADS * NUM_ITERATIONS);
//        Transfer transfer = new Transfer(null, null, null);
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
                        new Transfer(accountDao, fromAcct, toAcct, amount).run();
//                        transfer.run(fromAcct, toAcct, amount);
                    } finally {
                        latch.countDown();
                    }
                }
            }
        }

        long start = System.nanoTime();
        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        long duration = System.nanoTime() - start;
        log.info("duration:" + (((float) duration) / 1000_000));
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(in1);
//        System.out.println(in2);
//        System.out.println(Transfer.in1);
//        System.out.println(Transfer.in2);
    }
}
