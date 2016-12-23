package pack.transfer.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pack.transfer.api.AccountDao;
import pack.transfer.service.Transfer;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class DemonstrateNoDeadlockTest {
    private static final Logger log = LogManager.getLogger();

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000_000;

    private static final Random rnd = new Random();

    private static final AccountDao accountDaoStub = new AccountDao() {
        @Override
        public void create(String number, String balance, String currency, Long userId, boolean active, String limit) {
        }

        @Override
        public Account findAccount(String number) {
            return new Account(number, String.valueOf(rnd.nextInt(1000) + 200_000_000), null, null, true, String.valueOf(rnd.nextInt(1000) + 300));
        }

        @Override
        public void updateAccountsBalance(String accNum1, String balance1, String accNum2, String balance2) {
        }
    };

    public static void main(String[] args) {
        final Account[] accounts = new Account[NUM_ACCOUNTS];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account("num" + i, String.valueOf(rnd.nextInt(1000) + 200_000_000), null, null, true, String.valueOf(rnd.nextInt
                    (1000) + 300));
        }
        final CountDownLatch latch = new CountDownLatch(NUM_THREADS * NUM_ITERATIONS);
        class TransferThread extends Thread {
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromInd = rnd.nextInt(NUM_ACCOUNTS);
                    int toInd = rnd.nextInt(NUM_ACCOUNTS);
                    Account fromAcct = accounts[fromInd];
                    Account toAcct = accounts[toInd];
                    while (fromAcct.getNumber().equals(toAcct.getNumber())) {
                        toInd = rnd.nextInt(NUM_ACCOUNTS);
                        toAcct = accounts[toInd];
                    }
                    try {
                        new Transfer(accountDaoStub, fromAcct, toAcct, new BigDecimal(rnd.nextInt(100))).run();
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
    }
}
