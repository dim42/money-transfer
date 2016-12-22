package test.transfer.service;

import test.transfer.api.AccountDao;
import test.transfer.api.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountDao dao;

    public AccountServiceImpl(AccountDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(String number, String balance, String currency, Long userId, boolean active, String limit) {
        dao.create(number, balance, currency, userId, active, limit);
    }
}
