package test.transfer.service;

import test.transfer.api.AccountDao;
import test.transfer.api.TransferService;
import test.transfer.model.Account;

import java.math.BigDecimal;

public class TransferServiceImpl implements TransferService {
    private final AccountDao accountDao;

    public TransferServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String from, String to, BigDecimal amount) {
        Account fromAcct = accountDao.findAccount(from);
        fromAcct.checkActive();
        fromAcct.checkLimit(amount);
        Account toAcct = accountDao.findAccount(from);
        toAcct.checkActive();
        new Transfer(accountDao, fromAcct, toAcct, amount).run();
    }
}
