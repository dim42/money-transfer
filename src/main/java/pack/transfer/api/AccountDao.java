package pack.transfer.api;

import pack.transfer.model.Account;

public interface AccountDao {
    void create(String number, String balance, String currency, Long userId, boolean active, String limit);

    Account findAccount(String number);

    void updateAccountsBalance(Account acc1, Account acc2);
}
