package test.transfer.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

import static java.util.Objects.requireNonNull;
import static test.transfer.util.Utils.requireNonBlank;

@XmlRootElement
public class AccountRequest {
    private String number;
    private String balance;
    private String currency;
    private Long userId;
    private boolean isActive;
    private String limit;

    // JAXB needs this
    public AccountRequest() {
    }

    public AccountRequest(String number, String balance, String currency, Long userId, boolean isActive, String limit) {
        this.number = number;
        this.balance = balance;
        this.currency = currency;
        this.userId = userId;
        this.isActive = isActive;
        this.limit = limit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void validate() {
        requireNonBlank(number, "Account number is empty");
        requireNonNull(userId, "Account userId is null");
    }
}
