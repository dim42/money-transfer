package pack.transfer.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.DOWN;
import static pack.transfer.util.Utils.requireNonBlank;

@XmlRootElement
public class TransferRequest {

    private String from;
    private String to;
    private String amount;
    private String cur;
    private BigDecimal amountNum;

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

    public BigDecimal getAmountNum() {
        return amountNum;
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

    public void validate() {
        requireNonBlank(from, "From account is empty");
        requireNonBlank(to, "To account is empty");
        requireNonBlank(amount, "Amount is empty");
        requireNonBlank(cur, "Currency is empty");
        if (from.equals(to)) {
            throw new IllegalArgumentException("Accounts are equal");
        }
        amountNum = new BigDecimal(this.amount).setScale(2, DOWN);
        if (amountNum.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("Amount is negative");
        }
    }
}
