package test.transfer;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class Account {

    private String number;
    private String currency;
    private BigDecimal amount;

    public static void main(String[] args) {
        Account account1 = new Account();
        account1.amount = new BigDecimal(14.567).setScale(2, HALF_UP);
        Account account2 = new Account();
        account2.amount = new BigDecimal(8.34).setScale(2, HALF_UP);
        System.out.println(account1.amount);
        System.out.println(account2.amount);

        account1.transferMoney(account2, new BigDecimal(7.4).setScale(1, HALF_UP));

        System.out.println(account1.amount);
        System.out.println(account2.amount);
        assert 0 == account1.amount.compareTo(new BigDecimal(7.17).setScale(2, HALF_UP));
        assert 0 == account2.amount.compareTo(new BigDecimal(15.74).setScale(2, HALF_UP));
    }

    public void transferMoney(Account to, BigDecimal transferAmount) {
        if (amount != null && amount.compareTo(transferAmount) > 0) {
            amount = amount.subtract(transferAmount);
            to.amount = to.amount == null ? transferAmount : to.amount.add(transferAmount);
        }
    }
}
