package test.transfer.model;

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
//    public void transferMoney(BigDecimal balance) {
//        BigDecimal fromAmount = from.getAmount();
//        if (fromAmount != null && fromAmount.compareTo(balance) > 0) {
//            from.setAmount(fromAmount.subtract(balance));
//            BigDecimal toAmount = to.getAmount();
//            to.setAmount(toAmount == null ? balance : toAmount.add(balance));
//        }
//    }

    public void transferMoney(BigDecimal transferAmount) {
//        BigDecimal fromAmount = from.getAmount();
//        if (fromAmount != null && fromAmount.compareTo(balance) > 0) {
//            from.setAmount(fromAmount.subtract(balance));
//            BigDecimal toAmount = to.getAmount();
//            to.setAmount(toAmount == null ? balance : toAmount.add(balance));
//        }
        class Helper {
            private void doTransfer() {
                if (from.balance != null && from.balance.compareTo(transferAmount) > 0) {
                    from.balance = from.balance.subtract(transferAmount);
                    to.balance = to.balance == null ? transferAmount : to.balance.add(transferAmount);
                }
            }
        }

        if (from.number.compareTo(to.number) > 0) {
            synchronized (from) {
                synchronized (to) {
                    doTransfer(from, to, transferAmount);
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

    private void doTransfer(Account fromAcct, Account toAcct, BigDecimal amount) {
        fromAcct.checkInsufficientBalance(amount);
        fromAcct.debit(amount);
        toAcct.credit(amount);
//        accountService.update(fromAcct, toAcct)
//        if (fromAcct.balance != null && fromAcct.balance.compareTo(balance) > 0) {
//            fromAcct.balance = fromAcct.balance.subtract(balance);
//            toAcct.balance = toAcct.balance == null ? balance : toAcct.balance.add(balance);
//        }
    }

    public void transferMoney(final Account from, final Account to, final BigDecimal transferAmount) {
        if (from.number.equals(to.number)) {
//            throw new RuntimeException("The same account number.");
            return;
        }

        class Helper {
            private void doTransfer() {
                if (from.balance != null && from.balance.compareTo(transferAmount) > 0) {
                    from.balance = from.balance.subtract(transferAmount);
                    to.balance = to.balance == null ? transferAmount : to.balance.add(transferAmount);
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
