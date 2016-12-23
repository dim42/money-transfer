package pack.transfer.model;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.DOWN;

public class Account {

    private final String number;
    private BigDecimal balance;
    private final String currency;
    private final Long userId;
    private boolean isActive;
    private BigDecimal limit;

    public Account(String number, String balance, String currency, Long userId, boolean isActive, String limit) {
        this.number = number;
        this.balance = new BigDecimal(balance).setScale(2, DOWN);
        this.currency = currency;
        this.userId = userId;
        this.isActive = isActive;
        this.limit = new BigDecimal(limit).setScale(2, DOWN);
    }

    public BigDecimal getBalance() {
        return balance != null ? balance : ZERO;
    }

    public String getNumber() {
        return number;
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
