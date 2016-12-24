package pack.transfer.service;

import pack.transfer.api.CurRateDao;
import pack.transfer.api.CurRateService;
import pack.transfer.model.CurRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.HALF_UP;

public class CurRateServiceImpl implements CurRateService {
    private static final String DELIMITER = "_";
    private final CurRateDao dao;
    private final Map<String, BigDecimal> cr;

    public CurRateServiceImpl(CurRateDao dao) {
        this.dao = dao;
        cr = new ConcurrentHashMap<>();
        sync();
    }

    @Override
    public void create(int id, String c2c, String rate) {
        sync();
        dao.create(id, c2c, rate);
    }

    @Override
    public void sync() {
        List<CurRate> curRates = dao.allCurRates();
        curRates.forEach(curRate -> {
            BigDecimal rate = new BigDecimal(curRate.getRate()).setScale(4, HALF_UP);
            cr.put(curRate.getC2c(), rate);
        });
    }

    @Override
    public BigDecimal convert(String currencyFrom, String currencyTo, BigDecimal amount) {
        if (currencyFrom.equals(currencyTo)) {
            return amount;
        }
        BigDecimal curRate = cr.get(currencyFrom + DELIMITER + currencyTo);
        return amount.multiply(curRate).setScale(2, DOWN);
    }
}
