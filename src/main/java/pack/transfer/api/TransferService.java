package pack.transfer.api;

import java.math.BigDecimal;

public interface TransferService {
    void transfer(String from, String to, BigDecimal amount);
}
