package test.transfer.service;

import test.transfer.api.AccountDao;
import test.transfer.model.Account;

import java.math.BigDecimal;

public class Transfer {

    private final AccountDao accountDao;
    private final Account fromAcct;
    private final Account toAcct;
    private final BigDecimal amount;

    public Transfer(AccountDao accountDao, Account fromAcct, Account toAcct, BigDecimal amount) {
        this.accountDao = accountDao;
        this.fromAcct = fromAcct;
        this.toAcct = toAcct;
        this.amount = amount;
    }

    public void run() {
        if (fromAcct.greater(toAcct)) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
                    doTransfer();
                }
            }
        } else {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    doTransfer();
                }
            }
        }
    }

    private void doTransfer() {
        fromAcct.checkInsufficientBalance(amount);
        fromAcct.debit(amount);
        toAcct.credit(amount);
        accountDao.updateAccountsBalance(fromAcct.getNumber(), fromAcct.getBalance().toString(), toAcct.getNumber(), toAcct.getBalance().toString());
    }
}
