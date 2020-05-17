package pack.transfer.rest;

import pack.transfer.api.AccountDao;
import pack.transfer.api.AccountService;
import pack.transfer.api.CurRateService;
import pack.transfer.api.TransferService;
import pack.transfer.api.UserDao;
import pack.transfer.api.UserService;
import pack.transfer.dao.AccountDaoOrmLite;
import pack.transfer.dao.CurRateDaoImpl;
import pack.transfer.dao.DBManagerImpl;
import pack.transfer.dao.UserDaoImpl;
import pack.transfer.dao.UserDaoOrmLite;
import pack.transfer.service.AccountServiceImpl;
import pack.transfer.service.CurRateServiceImpl;
import pack.transfer.service.TransferServiceImpl;
import pack.transfer.service.UserServiceImpl;
import pack.transfer.util.PropertiesHelper;

import java.util.Properties;

public class AppContext {
    private static final String PROP_FILE_NAME = "sql.xml";

    private static UserService userService;
    private static AccountService accountService;
    private static CurRateService curRateService;
    private static TransferServiceImpl transferService;

    static void init() {
        init(new Properties());
    }

    static void init(Properties properties) {
        PropertiesHelper prop = new PropertiesHelper(AppContext.class, PROP_FILE_NAME, properties);
        DBManagerImpl dbManager = new DBManagerImpl(prop);
        UserDao daoImpl = new UserDaoImpl(dbManager);
        UserDao userDaoOrm = new UserDaoOrmLite(prop);
        userService = new UserServiceImpl(userDaoOrm);
//        AccountDao accountDao = new AccountDaoImpl(dbManager);
        AccountDao accountDao = new AccountDaoOrmLite(prop, userDaoOrm);
        accountService = new AccountServiceImpl(accountDao);
        curRateService = new CurRateServiceImpl(new CurRateDaoImpl(dbManager));
        transferService = new TransferServiceImpl(accountDao, curRateService);
    }

    public static CurRateService getCurRateService() {
        return curRateService;
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
