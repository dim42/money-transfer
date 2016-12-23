package pack.transfer.service;

import pack.transfer.api.AccountDao;
import pack.transfer.api.AccountService;
import pack.transfer.model.Account;

public class AccountServiceImpl implements AccountService {
    private final AccountDao dao;

    public AccountServiceImpl(AccountDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(String number, String balance, String currency, Long userId, boolean active, String limit) {
        dao.create(number, balance, currency, userId, active, limit);
    }

    @Override
    public Account find(String number) {
        return dao.findAccount(number);
    }
}
