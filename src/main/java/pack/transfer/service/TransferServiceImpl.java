package pack.transfer.service;

import pack.transfer.model.Account;
import pack.transfer.api.AccountDao;
import pack.transfer.api.TransferService;

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
        Account toAcct = dao.findAccount(to);
        toAcct.checkActive();
        new Transfer(dao, fromAcct, toAcct, amount).run();
    }
}
