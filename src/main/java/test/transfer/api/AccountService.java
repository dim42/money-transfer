package test.transfer.api;

import test.transfer.model.Account;

public interface AccountService {
    void create(String number, String balance, String currency, Long userId, boolean active, String limit);

    Account find(String number);
}
