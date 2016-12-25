package pack.transfer.api;

import pack.transfer.model.User;

public interface UserDao {
    void createUser(Long id, String name);

    User findUser(Long userId);
}
