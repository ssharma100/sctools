package com.oracle.ofsc.etadirect.soap;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Simple encapsulation for id elements
 */
@XmlType
public class ManagedProfile {

    private List<String> values;

    public ManagedProfile() {}

    public ManagedProfile(List<String> values) {
        this.values = values;
    }

    @XmlElement(name="profile")
    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
