package test.transfer.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommonResponse {

    private String resultCode;
    private String message;

    // JAXB needs this
    public CommonResponse() {
    }

    public CommonResponse(ResultCode resultCode, String message) {
        this.resultCode = resultCode.toString();
        this.message = message;
    }

    public CommonResponse(ResultCode resultCode) {
        this(resultCode, null);
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
