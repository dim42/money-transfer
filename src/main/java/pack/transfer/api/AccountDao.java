package pack.transfer.api;

import pack.transfer.model.Account;

public interface AccountDao {
    void create(String number, String balance, String currency, Long userId, boolean active, String limit);

    Account findAccount(String number);

    void updateAccountsBalance(String accNum1, String balance1, String accNum2, String balance2);
}
