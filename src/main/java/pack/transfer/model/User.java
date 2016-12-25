package pack.transfer.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static pack.transfer.model.User.USERS_TABLE;

@DatabaseTable(tableName = USERS_TABLE)
public class User {
    public static final String USERS_TABLE = "users";

    @DatabaseField(id = true)
    private Long id;
    @DatabaseField
    private String name;

    // for ORMLite
    public User() {
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
