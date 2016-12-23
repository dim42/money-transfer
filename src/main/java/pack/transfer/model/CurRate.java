package pack.transfer.model;

public class CurRate {
    private final Long id;
    private final String c2c;
    private final String rate;

    public CurRate(Long id, String c2c, String rate) {
        this.id = id;
        this.c2c = c2c;
        this.rate = rate;
    }

    public String getC2c() {
        return c2c;
    }

    public String getRate() {
        return rate;
    }
}
