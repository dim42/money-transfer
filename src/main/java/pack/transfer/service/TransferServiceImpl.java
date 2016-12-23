package pack.transfer.service;

import pack.transfer.api.AccountDao;
import pack.transfer.api.CurRateService;
import pack.transfer.api.TransferService;
import pack.transfer.model.Account;

import java.math.BigDecimal;

public class TransferServiceImpl implements TransferService {
    private final AccountDao dao;
    private final CurRateService curRateService;

    public TransferServiceImpl(AccountDao dao, CurRateService curRateService) {
        this.dao = dao;
        this.curRateService = curRateService;
    }

    @Override
    public void transfer(String from, String to, String currency, BigDecimal amount) {
        Account fromAcct = dao.findAccount(from);
        fromAcct.checkActive();
        fromAcct.checkLimit(amount);
        Account toAcct = dao.findAccount(to);
        toAcct.checkActive();
        curRateService.sync();
        BigDecimal fromAmount = curRateService.convert(currency, fromAcct.getCurrency(), amount);
        BigDecimal toAmount = curRateService.convert(toAcct.getCurrency(), currency, amount);
        new Transfer(dao, fromAcct, fromAmount, toAcct, toAmount).run();
    }
}
