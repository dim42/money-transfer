package pack.transfer.rest;

import pack.transfer.api.AccountService;
import pack.transfer.api.DBManager;
import pack.transfer.api.TransferService;
import pack.transfer.dao.UserDaoImpl;
import pack.transfer.service.AccountServiceImpl;
import pack.transfer.service.TransferServiceImpl;
import pack.transfer.service.UserServiceImpl;
import pack.transfer.api.UserService;
import pack.transfer.dao.AccountDaoImpl;
import pack.transfer.dao.DBManagerImpl;

public class AppContext {
    private static final UserService userService;
    private static final TransferServiceImpl transferService;
    private static final AccountService accountService;

    static {
        DBManagerImpl dbManager = new DBManagerImpl(DBManager.PROP_FILE_NAME);
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
