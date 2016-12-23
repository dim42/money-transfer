package pack.transfer.api;

import java.math.BigDecimal;

public interface CurRateService {
    void create(int id, String c2c, String rate);

    void sync();

    BigDecimal convert(String currencyFrom, String currencyTo, BigDecimal amount);
}
