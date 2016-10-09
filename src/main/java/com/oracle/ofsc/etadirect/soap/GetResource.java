package com.oracle.ofsc.etadirect.soap;

import org.apache.camel.Exchange;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Created by Samir on 10/6/2016.
 */
@XmlRootElement(name="get_resource")
@XmlSeeAlso(User.class)
public class GetResource {

    private User user;

    @XmlElement(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
