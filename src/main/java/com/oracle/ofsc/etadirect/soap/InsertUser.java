package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * SOAP body generator for inserting a user
 */
@XmlRootElement(name="urn:insert_user")
@XmlType(propOrder = { "user", "login" })
@XmlSeeAlso({User.class, Property.class})
public class InsertUser {

    private User user;
    private String login;
    private List<Property> properties;

    public InsertUser () {

    }
    @XmlElement(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @XmlElement(name="login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    @XmlElement(name="properties")
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
