package step.learning.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String phone;
    private String email;
    private String filename;
    private String passwordSalt;
    private String passwordDk; // Derived Key (https://datatracker.ietf.org/doc/html/rfc2898)

    public User(ResultSet resultSet) throws SQLException {
        setId(UUID.fromString(resultSet.getString("id")));
        setName(resultSet.getString("name"));
        setPhone(resultSet.getString("phone"));
        setEmail(resultSet.getString("email"));
        setFilename(resultSet.getString("avatar"));
        setPasswordSalt(resultSet.getString("salt"));
        setPasswordDk(resultSet.getString("dk"));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordDk() {
        return passwordDk;
    }

    public void setPasswordDk(String passwordDk) {
        this.passwordDk = passwordDk;
    }
}

/*
* Login: First Password:First
* Login: Second Password:Second
* Login: Third Password:Third
* Login: Forth Password:Forth
* Login: Admin Password:admin
*/