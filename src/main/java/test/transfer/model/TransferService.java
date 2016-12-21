package test.transfer.model;

import java.math.BigDecimal;

public class TransferService {

    private static final Object tieLock = new Object();
    private final Account fromAcct;
    private final Account toAcct;

//    static AtomicInteger in1 = new AtomicInteger(0);
//    static AtomicInteger in2 = new AtomicInteger(0);

    public TransferService(Account fromAcct, Account toAcct) {
        this.fromAcct = fromAcct;
        this.toAcct = toAcct;
    }

//    public void transferMoney(BigDecimal balance) {
//        BigDecimal fromAmount = fromAcct.getAmount();
//        if (fromAmount != null && fromAmount.compareTo(balance) > 0) {
//            fromAcct.setAmount(fromAmount.subtract(balance));
//            BigDecimal toAmount = toAcct.getAmount();
//            toAcct.setAmount(toAmount == null ? balance : toAmount.add(balance));
//        }
//    }

    public void transferMoney(BigDecimal transferAmount) {
        if (fromAcct.greater(toAcct)) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
//                    in1.incrementAndGet();
                    doTransfer(transferAmount);
                }
            }
        } else {
            synchronized (toAcct) {
                synchronized (fromAcct) {
//                    in2.incrementAndGet();
//                    doTransfer(fromAcct, toAcct, transferAmount);
                    doTransfer(transferAmount);
                }
            }
        }
    }

    //    private void doTransfer(Account fromAcct, Account toAcct, BigDecimal amount) {
    private void doTransfer(BigDecimal amount) {
        fromAcct.checkInsufficientBalance(amount);
        fromAcct.debit(amount);
        toAcct.credit(amount);
    }

    public void transferMoney(final Account fromAcct, final Account toAcct, final BigDecimal transferAmount) {
        if (fromAcct.number.equals(toAcct.number)) {
            throw new RuntimeException("The same account number.");
//            return;
        }

        class Helper {
            private void doTransfer() {
//                if (fromAcct.balance != null && fromAcct.balance.compareTo(transferAmount) > 0) {
//                    fromAcct.balance = fromAcct.balance.subtract(transferAmount);
//                    toAcct.balance = toAcct.balance == null ? transferAmount : toAcct.balance.add(transferAmount);
//                }
                if (fromAcct.balance() != null && fromAcct.balance().compareTo(transferAmount) > 0) {
                    fromAcct.balance(fromAcct.balance().subtract(transferAmount));
                    toAcct.balance(toAcct.balance() == null ? transferAmount : toAcct.balance().add(transferAmount));
                }
            }
        }

        if (fromAcct.number.compareTo(toAcct.number) > 0) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
//                    doTransfer(fromAcct, toAcct, transferAmount);
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
