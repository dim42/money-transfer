package test.transfer.dao.api;

import test.transfer.model.Account;

public interface AccountDao {
    Account findAccount(String accNum);

    void upAccBals(String accNum1, String balance1, String accNum2, String balance2);
}
