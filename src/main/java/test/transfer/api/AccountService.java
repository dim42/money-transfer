package test.transfer.api;

public interface AccountService {
    void create(String number, String balance, String currency, Long userId, boolean active, String limit);
}
