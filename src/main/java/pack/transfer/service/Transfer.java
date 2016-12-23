package pack.transfer.service;

import pack.transfer.api.AccountDao;
import pack.transfer.model.Account;

import java.math.BigDecimal;

public class Transfer {

    private final AccountDao accountDao;
    private final Account fromAcct;
    private final BigDecimal fromAmount;
    private final Account toAcct;
    private final BigDecimal toAmount;

    public Transfer(AccountDao accountDao, Account fromAcct, BigDecimal fromAmount, Account toAcct, BigDecimal toAmount) {
        this.accountDao = accountDao;
        this.fromAcct = fromAcct;
        this.fromAmount = fromAmount;
        this.toAcct = toAcct;
        this.toAmount = toAmount;
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
        fromAcct.checkInsufficientBalance(fromAmount);
        fromAcct.debit(fromAmount);
        toAcct.credit(toAmount);
        accountDao.updateAccountsBalance(fromAcct.getNumber(), fromAcct.getBalance().toString(), toAcct.getNumber(), toAcct.getBalance().toString());
    }
}
