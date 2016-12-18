package test.transfer;

import java.math.BigDecimal;
import java.util.Random;

import static java.math.RoundingMode.HALF_UP;

public class Account {

    private final String number;
    BigDecimal amount;
    private String currency;

    public Account(String number) {
        this.number = number;
    }

    public static void main(String[] args) {
        Account account1 = new Account("123");
        account1.amount = new BigDecimal(14.567).setScale(2, HALF_UP);
        Account account2 = new Account("124");
        account2.amount = new BigDecimal(8.34).setScale(2, HALF_UP);
        System.out.println(account1.amount);
        System.out.println(account2.amount);

        account1.transferMoney(account2, new BigDecimal(7.4).setScale(1, HALF_UP));

        System.out.println(account1.amount);
        System.out.println(account2.amount);
        assert 0 == account1.amount.compareTo(new BigDecimal(7.17).setScale(2, HALF_UP));
        assert 0 == account2.amount.compareTo(new BigDecimal(15.74).setScale(2, HALF_UP));
    }

    public void transferMoney1(Account to, BigDecimal transferAmount) {
        synchronized (this) {
            synchronized (to) {
                if (amount != null && amount.compareTo(transferAmount) > 0) {
                    amount = amount.subtract(transferAmount);
                    to.amount = to.amount == null ? transferAmount : to.amount.add(transferAmount);
                }
            }
        }
    }

    public void transferMoney2(Account to, BigDecimal transferAmount) {
        System.out.println(this.amount + " " + to.amount + " " + transferAmount);
        if (amount != null && amount.compareTo(transferAmount) > 0) {
            amount = amount.subtract(transferAmount);
            to.amount = to.amount == null ? transferAmount : to.amount.add(transferAmount);
        }
        System.out.println(this.amount + " " + to.amount + "\n");
    }

    public void transferMoney(final Account to, BigDecimal transferAmount) {
        if (number.equals(to.number)) {
//            throw new RuntimeException("The same account number.");
            return;
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
        if (amount != null && amount.compareTo(transferAmount) > 0) {
            amount = amount.subtract(transferAmount);
            to.amount = to.amount == null ? transferAmount : to.amount.add(transferAmount);
        }
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
            accounts[i].amount = new BigDecimal(rnd.nextInt(1000) + 200);
        }
        class TransferThread extends Thread {
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
//                DollarAmount amount = new DollarAmount(rnd.nextInt(1000));
                    BigDecimal amount = new BigDecimal(rnd.nextInt(100));
                    accounts[fromAcct].transferMoney(accounts[toAcct], amount);
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
    }
}
