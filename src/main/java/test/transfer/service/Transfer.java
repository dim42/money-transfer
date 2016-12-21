package test.transfer.service;

import test.transfer.api.AccountDao;
import test.transfer.model.Account;

import java.math.BigDecimal;

public class Transfer {

    private static final Object tieLock = new Object();
    private final AccountDao accountDao;
    private final Account fromAcct;
    private final Account toAcct;
    private final BigDecimal amount;

//    static AtomicInteger in1 = new AtomicInteger(0);
//    static AtomicInteger in2 = new AtomicInteger(0);

    public Transfer(AccountDao accountDao, Account fromAcct, Account toAcct, BigDecimal amount) {
        this.accountDao = accountDao;
        this.fromAcct = fromAcct;
        this.toAcct = toAcct;
        this.amount = amount;
    }

//    public void run(BigDecimal balance) {
//        BigDecimal fromAmount = fromAcct.getAmount();
//        if (fromAmount != null && fromAmount.compareTo(balance) > 0) {
//            fromAcct.setAmount(fromAmount.subtract(balance));
//            BigDecimal toAmount = toAcct.getAmount();
//            toAcct.setAmount(toAmount == null ? balance : toAmount.add(balance));
//        }
//    }

    public void run() {
//        if (fromAcct.getNumber().equals(toAcct.getNumber())) {
//            throw new RuntimeException("The same account number.");
////            return;
//        }
        if (fromAcct.greater(toAcct)) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
//                    in1.incrementAndGet();
                    doTransfer();
                }
            }
        } else {
            synchronized (toAcct) {
                synchronized (fromAcct) {
//                    in2.incrementAndGet();
//                    doTransfer(fromAcct, toAcct, amount);
                    doTransfer();
                }
            }
        }
    }

    //    private void doTransfer(Account fromAcct, Account toAcct, BigDecimal amount) {
    private void doTransfer() {
        fromAcct.checkInsufficientBalance(amount);
        fromAcct.debit(amount);
        toAcct.credit(amount);
        accountDao.updateAccountsBalance(fromAcct.getNumber(), fromAcct.getBalance().toString(), toAcct.getNumber(), toAcct.getBalance().toString());
    }

    public void run(final Account fromAcct, final Account toAcct, final BigDecimal amount) {
        if (fromAcct.getNumber().equals(toAcct.getNumber())) {
            throw new RuntimeException("The same account number.");
//            return;
        }

        class Helper {
            private void doTransfer() {
//                if (fromAcct.balance != null && fromAcct.balance.compareTo(amount) > 0) {
//                    fromAcct.balance = fromAcct.balance.subtract(amount);
//                    toAcct.balance = toAcct.balance == null ? amount : toAcct.balance.add(amount);
//                }
//                if (fromAcct.balance() != null && fromAcct.balance().compareTo(amount) > 0) {
//                    fromAcct.balance(fromAcct.balance().subtract(amount));
//                    toAcct.balance(toAcct.balance() == null ? amount : toAcct.balance().add(amount));
//                }
                fromAcct.checkInsufficientBalance(amount);
                fromAcct.debit(amount);
                toAcct.credit(amount);
            }
        }

//        if (fromAcct.getNumber().compareTo(toAcct.getNumber()) > 0) {
        if (fromAcct.greater(toAcct)) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
//                    doTransfer(fromAcct, toAcct, amount);
                    new Helper().doTransfer();
                }
            }
        } else {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    new Helper().doTransfer();
                }
            }
        }
//        in1.incrementAndGet();
    }


    public void transferMoney2(final Account fromAcct,
                               final Account toAcct,
                               final BigDecimal amount) {
        class Helper {
            public void transfer() {
                if (fromAcct.getBalance().compareTo(amount) < 0) {
//                    throw new InsufficientFundsException();
                    throw new RuntimeException("InsufficientFundsException");
                } else {
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }
        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);
        if (fromHash < toHash) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAcct) {
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }
}
