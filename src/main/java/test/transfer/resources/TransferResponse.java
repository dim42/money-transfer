package test.transfer.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferResponse {

    private String resultCode;
    private String message;

    // JAXB needs this
    public TransferResponse() {
    }

    public TransferResponse(ResultCode resultCode, String message) {
        this.resultCode = resultCode.toString();
        this.message = message;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
