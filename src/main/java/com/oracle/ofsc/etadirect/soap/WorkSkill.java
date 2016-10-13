package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by xxx_sharma on 10/13/16.
 */
@SuppressWarnings("unused")
@XmlType(propOrder = { "label", "ratio" })
public class WorkSkill {

    private String label;
    private String ratio;

    public WorkSkill() {

    }
    @XmlElement(name="label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name="value")
    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
