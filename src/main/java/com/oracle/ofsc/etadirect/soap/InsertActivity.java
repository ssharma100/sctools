package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * SOAP model for the insert activity request.
 */
@XmlRootElement(name="urn:create_activity")
@XmlType(propOrder = { "user", "date", "bucketId", "posInRoute", "properties"})
@XmlSeeAlso({User.class, Property.class, WorkSkill.class})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class InsertActivity {

    private User user;
    private String date;  // Date only: 2010-11-28
    private String bucketId;
    private String posInRoute = "unordered";
    private List<Property> properties;

    public InsertActivity() { }

    @XmlElement(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @XmlElement(name="user")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @XmlElement(name="resource_id")
    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }
    @XmlElement(name="position_in_route")
    public String getPosInRoute() {
        return posInRoute;
    }

    public void setPosInRoute(String posInRoute) {
        this.posInRoute = posInRoute;
    }

    @XmlElement(name="properties")
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
