package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.*;

/**
 * Created by xxx_sharma on 10/12/16.
 */
@XmlType(propOrder = { "name", "value" })
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Property {

    private String name;
    private String value;

    public Property () {}
    public Property (String name, String value) {
        this.name = name;
        this.value = value;
    }

    @XmlElement(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
