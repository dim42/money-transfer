package test.transfer.model;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

public class Account {

    final String number;
    //    BigDecimal balance;
    private BigDecimal balance;

    BigDecimal balance() {
        return balance;
    }

    void balance(BigDecimal balance) {
        this.balance = balance;
    }

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

//    static AtomicInteger in1 = new AtomicInteger(0);
//    static AtomicInteger in2 = new AtomicInteger(0);

    public void transferMoney(final Account to, BigDecimal transferAmount) {
//        in1.incrementAndGet();
        if (number.equals(to.number)) {
//            in2.incrementAndGet();
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
        if (balance != null && balance.compareTo(transferAmount) >= 0) {
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
