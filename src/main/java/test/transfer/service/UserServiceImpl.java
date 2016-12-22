package test.transfer.service;

import test.transfer.api.UserDao;
import test.transfer.api.UserService;

public class UserServiceImpl implements UserService {
    private final UserDao dao;

    public UserServiceImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(long id, String name) {
        dao.createUser(id, name);
    }
}
