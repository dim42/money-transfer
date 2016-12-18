package test.transfer;

import java.math.BigDecimal;

public class Transfer {

    private static final Object tieLock = new Object();
    private final Account from;
    private final Account to;

    public Transfer(Account from, Account to) {
        this.from = from;
        this.to = to;
    }

    //    public static void main(String[] args) {
//        Account account1 = new Account();
//        account1.setAmount(new BigDecimal(14.567));
//        Account account2 = new Account();
//        account2.setAmount(new BigDecimal(8.34));
//
//        new Transfer(account1, account2).transferMoney(new BigDecimal(7.4));
//
//        System.out.println(account1.getAmount());
//        System.out.println(account2.getAmount());
//    }
//
//    public void transferMoney(BigDecimal amount) {
//        BigDecimal fromAmount = from.getAmount();
//        if (fromAmount != null && fromAmount.compareTo(amount) > 0) {
//            from.setAmount(fromAmount.subtract(amount));
//            BigDecimal toAmount = to.getAmount();
//            to.setAmount(toAmount == null ? amount : toAmount.add(amount));
//        }
//    }
    public void transferMoney(final Account from, final Account to, final BigDecimal transferAmount) {
        if (from.number.equals(to.number)) {
//            throw new RuntimeException("The same account number.");
            return;
        }

        class Helper {
            private void doTransfer() {
                if (from.amount != null && from.amount.compareTo(transferAmount) > 0) {
                    from.amount = from.amount.subtract(transferAmount);
                    to.amount = to.amount == null ? transferAmount : to.amount.add(transferAmount);
                }
            }
        }

        if (from.number.compareTo(to.number) > 0) {
            synchronized (from) {
                synchronized (to) {
//                    doTransfer(from, to, transferAmount);
                    new Helper().doTransfer();
                }
            }
        } else {
            synchronized (to) {
                synchronized (from) {
                    new Helper().doTransfer();
                }
            }
        }
    }


    public void transferMoney2(final Account fromAcct,
                               final Account toAcct,
                               final BigDecimal amount) {
        class Helper {
            public void transfer() {
                if (fromAcct.getBalance().compareTo(amount) < 0) ;
//                    throw new InsufficientFundsException();
                else {
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
