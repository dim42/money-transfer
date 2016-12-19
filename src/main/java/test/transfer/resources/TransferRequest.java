package test.transfer.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferRequest {

    private String from;
    private String to;
    private String amount;
    private String cur;

    // JAXB needs this
    public TransferRequest() {
    }

    public TransferRequest(String from, String to, String amount, String cur) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.cur = cur;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }
}
