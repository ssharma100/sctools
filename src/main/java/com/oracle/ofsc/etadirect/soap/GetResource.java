package com.oracle.ofsc.etadirect.soap;

import org.apache.camel.Exchange;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Samir on 10/6/2016.
 */
@XmlRootElement(name="get_resource")
@XmlType(propOrder = { "user", "id" })
@XmlSeeAlso(User.class)
public class GetResource {

    private User user;
    private String id;

    @XmlElement(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
