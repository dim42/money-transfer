package pack.transfer.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CurRateRequest {
    private int id;
    private String c2c;
    private String rate;

    // JAXB needs this
    public CurRateRequest() {
    }

    public CurRateRequest(int id, String c2c, String rate) {
        this.id = id;
        this.c2c = c2c;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getC2c() {
        return c2c;
    }

    public void setC2c(String c2c) {
        this.c2c = c2c;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
