package test.transfer.resources;

import javax.xml.bind.annotation.XmlRootElement;

import static test.transfer.util.Utils.requireNonBlank;

@XmlRootElement
public class UserRequest {
    private long id;
    private String name;

    // JAXB needs this
    public UserRequest() {
    }

    public UserRequest(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void validate() {
        requireNonBlank(name, "User name is empty");
    }
}
