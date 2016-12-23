package pack.transfer.api;

import pack.transfer.model.CurRate;

import java.util.List;

public interface CurRateDao {
    void create(int id, String c2c, String rate);

    List<CurRate> allCurRates();
}
