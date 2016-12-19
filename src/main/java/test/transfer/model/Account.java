package test.transfer.model;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public class Account {

    final String number;
    BigDecimal balance;
    private String currency;

    public Account(String number) {
        this.number = number;
    }

    public static void main(String[] args) {
        Account account1 = new Account("123");
        account1.balance = new BigDecimal(14.567).setScale(2, HALF_UP);
        Account account2 = new Account("124");
        account2.balance = new BigDecimal(8.34).setScale(2, HALF_UP);
        System.out.println(account1.balance);
        System.out.println(account2.balance);

        account1.transferMoney(account2, new BigDecimal(7.4).setScale(1, HALF_UP));

        System.out.println(account1.balance);
        System.out.println(account2.balance);
        assert 0 == account1.balance.compareTo(new BigDecimal(7.17).setScale(2, HALF_UP));
        assert 0 == account2.balance.compareTo(new BigDecimal(15.74).setScale(2, HALF_UP));
    }

    // deadlock
    public void transferMoney1(Account to, BigDecimal transferAmount) {
        synchronized (this) {
            synchronized (to) {
                if (balance != null && balance.compareTo(transferAmount) > 0) {
                    balance = balance.subtract(transferAmount);
                    to.balance = to.balance == null ? transferAmount : to.balance.add(transferAmount);
                }
            }
        }
    }

    public void transferMoney2(Account to, BigDecimal transferAmount) {
//        System.out.println(this.balance + " " + to.balance + " " + transferAmount);
        if (balance != null && balance.compareTo(transferAmount) > 0) {
            balance = balance.subtract(transferAmount);
            to.balance = to.balance == null ? transferAmount : to.balance.add(transferAmount);
        }
//        System.out.println(this.balance + " " + to.balance + "\n");
    }

    static AtomicInteger in1 = new AtomicInteger(0);
    static AtomicInteger in2 = new AtomicInteger(0);

    public void transferMoney(final Account to, BigDecimal transferAmount) {
        in1.incrementAndGet();
        if (number.equals(to.number)) {
            in2.incrementAndGet();
//            System.out.println("equals");
            throw new RuntimeException("The same account number.");
        }
        if (number.compareTo(to.number) > 0) {
            synchronized (this) {
                synchronized (to) {
                    doTransfer(to, transferAmount);
                }
            }
        } else {
            synchronized (to) {
                synchronized (this) {
                    doTransfer(to, transferAmount);
                }
            }
        }
    }

    private void doTransfer(Account to, BigDecimal transferAmount) {
        if (balance != null && balance.compareTo(transferAmount) > 0) {
            balance = balance.subtract(transferAmount);
            to.balance = to.balance == null ? transferAmount : to.balance.add(transferAmount);
        }
    }
//    private void doTransfer(Account toAcct, BigDecimal amount) {
//        checkInsufficientBalance(amount);
//        debit(amount);
//        toAcct.credit(amount);
//    }


    public BigDecimal getBalance() {
        return balance != null ? balance : ZERO;
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        balance = balance == null ? amount : balance.add(balance);
    }

    public void checkInsufficientBalance(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for account:" + this);
        }
    }

    public boolean greater(Account account) {
        return number.compareTo(account.number) > 0;
    }
}

class DemonstrateNoDeadlock {
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
