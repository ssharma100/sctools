package com.oracle.ofsc.etadirect.soap;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Simple encapsulation for id elements
 */
@XmlType
public class Profile {

    private List<String> values;

    public Profile() {}

    public Profile(List<String> values) {
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
