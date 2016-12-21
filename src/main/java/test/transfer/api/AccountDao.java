package test.transfer.api;

import test.transfer.model.Account;

public interface AccountDao {
    Account findAccount(String accNum);

    void updateAccountsBalance(String accNum1, String balance1, String accNum2, String balance2);
}
