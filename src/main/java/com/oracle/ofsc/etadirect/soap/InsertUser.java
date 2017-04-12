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
@XmlType(propOrder = { "user", "login", "properties", "resources", "profiles", "managedProfiles"})
@XmlSeeAlso({User.class, Property.class, Resource.class, Profile.class})
public class InsertUser {

    private User user;
    private String login;
    private Properties properties;
    private Resource resources;
    private Profile profiles;
    private ManagedProfile managedProfiles;

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

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @XmlElement
    public Resource getResources() {
        return resources;
    }

    public void setResources(Resource resources) {
        this.resources = resources;
    }

    public Profile getProfiles() {
        return profiles;
    }

    public void setProfiles(Profile profiles) {
        this.profiles = profiles;
    }

    @XmlElement(name="managed_profiles")
    public ManagedProfile getManagedProfiles() {
        return managedProfiles;
    }

    public void setManagedProfiles(ManagedProfile managedProfiles) {
        this.managedProfiles = managedProfiles;
    }
}
