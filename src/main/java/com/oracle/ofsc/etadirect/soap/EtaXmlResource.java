package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * SOAP message body for a resource creation
 */

@XmlRootElement(name="urn:insert_resource")
@XmlType(propOrder = { "user", "id", "properties", "workskills" })
@XmlSeeAlso({User.class, Property.class, WorkSkills.class})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class EtaXmlResource {

    private User user;
    private String id;
    private List<Property> properties;
    private WorkSkills workskills;

    public EtaXmlResource() {

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

    public WorkSkills getWorkskills() {
        return workskills;
    }

    public void setWorkskills(WorkSkills workskills) {
        this.workskills = workskills;
    }
}
