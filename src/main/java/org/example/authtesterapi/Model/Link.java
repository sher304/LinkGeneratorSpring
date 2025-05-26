package org.example.authtesterapi.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import org.example.authtesterapi.Constraint.URLNotFromDomain;

@Entity
public class Link {
    @Id
    private String id;
    @Size(min = 2, max = 20, message = "Name must be greater than 2 and less than 20")
    private String name;
    @URLNotFromDomain
    private String targetURL;
    private String password;
    private int visits;

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetURL() {
        return targetURL;
    }

    public void setTargetURL(String targetURL) {
        this.targetURL = targetURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
