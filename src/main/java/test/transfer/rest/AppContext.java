package test.transfer.rest;

import test.transfer.api.AccountService;
import test.transfer.api.TransferService;
import test.transfer.api.UserService;
import test.transfer.dao.AccountDaoImpl;
import test.transfer.dao.DBManagerImpl;
import test.transfer.dao.UserDaoImpl;
import test.transfer.service.AccountServiceImpl;
import test.transfer.service.TransferServiceImpl;
import test.transfer.service.UserServiceImpl;

import static test.transfer.api.DBManager.PROP_FILE_NAME;

public class AppContext {
    private static final UserService userService;
    private static final TransferServiceImpl transferService;
    private static final AccountService accountService;

    static {
        DBManagerImpl dbManager = new DBManagerImpl(PROP_FILE_NAME);
        AccountDaoImpl accountDao = new AccountDaoImpl(dbManager);
        transferService = new TransferServiceImpl(accountDao);
        userService = new UserServiceImpl(new UserDaoImpl(dbManager));
        accountService = new AccountServiceImpl(accountDao);
    }

    public static UserService getUserService() {
        return userService;
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    public static TransferService getTransferService() {
        return transferService;
    }
}
