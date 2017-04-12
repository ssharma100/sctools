package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType
public class Properties {

    List<Property> property;

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(List<Property> property) {
        this.property = property;
    }
}
