package test.transfer.service;

import test.transfer.api.AccountDao;
import test.transfer.api.TransferService;
import test.transfer.model.Account;

import java.math.BigDecimal;

public class TransferServiceImpl implements TransferService {
    private final AccountDao dao;

    public TransferServiceImpl(AccountDao dao) {
        this.dao = dao;
    }

    @Override
    public void transfer(String from, String to, BigDecimal amount) {
        Account fromAcct = dao.findAccount(from);
        fromAcct.checkActive();
        fromAcct.checkLimit(amount);
        Account toAcct = dao.findAccount(from);
        toAcct.checkActive();
        new Transfer(dao, fromAcct, toAcct, amount).run();
    }
}
