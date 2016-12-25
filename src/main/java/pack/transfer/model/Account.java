package pack.transfer.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.DOWN;
import static pack.transfer.model.Account.ACCOUNTS_TABLE;

@DatabaseTable(tableName = ACCOUNTS_TABLE)
public class Account {
    public static final String ACCOUNTS_TABLE = "accounts";

    @DatabaseField(id = true, columnName = "num")
    private String number;
    @DatabaseField
    private BigDecimal balance;
    @DatabaseField
    private String currency;
    @DatabaseField(foreign = true, canBeNull = false)
    private User user;
    private Long userId;
    @DatabaseField(columnName = "active")
    private boolean isActive;
    @DatabaseField(columnName = "lim")
    private BigDecimal limit;

    // for ORMLite
    public Account() {
    }

    public Account(String number, String balance, String currency, Long userId, boolean isActive, String limit) {
        this.number = number;
        this.balance = new BigDecimal(balance).setScale(2, DOWN);
        this.currency = currency;
        this.userId = userId;
        this.isActive = isActive;
        this.limit = new BigDecimal(limit).setScale(2, DOWN);
    }

    public Account(String number, String balance, String currency, User user, boolean active, String limit) {
        this(number, balance, currency, user.getId(), active, limit);
    }

    public BigDecimal getBalance() {
        return balance != null ? balance : ZERO;
    }

    public String getNumber() {
        return number;
    }

    public String getCurrency() {
        return currency;
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        balance = balance == null ? amount : balance.add(amount);
    }

    public boolean greater(Account account) {
        return number.compareTo(account.number) > 0;
    }

    public void checkInsufficientBalance(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException(format("Insufficient balance (%s) for account:%s, required %s", balance, number, amount));
        }
    }

    public void checkActive() {
        if (!isActive) {
            throw new RuntimeException(format("Account (%s) is not active", number));
        }
    }

    public void checkLimit(BigDecimal amount) {
        if (amount.compareTo(limit) > 0) {
            throw new RuntimeException(format("Transfer amount (%s) exceeds account's (%s) limit (%s)", amount, number, limit));
        }
    }
}
