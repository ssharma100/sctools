package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * SOAP message body for a resource creation
 */

@XmlRootElement(name="urn:insert_resource")
@XmlType(propOrder = { "user", "id", "properties", "workSkills" })
@XmlSeeAlso({User.class, Property.class, WorkSkill.class})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class InsertResource {

    private User user;
    private String id;
    private List<Property> properties;
    private List<WorkSkill> workSkills;

    public InsertResource () {

    }
    @XmlElement(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @XmlElement(name="id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElementWrapper(name="properties")
    @XmlElement(name="property")
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @XmlElementWrapper(name="workskills")
    @XmlElement(name="workskill")
    public List<WorkSkill> getWorkSkills() {
        return workSkills;
    }

    public void setWorkSkills(List<WorkSkill> workSkills) {
        this.workSkills = workSkills;
    }
}
