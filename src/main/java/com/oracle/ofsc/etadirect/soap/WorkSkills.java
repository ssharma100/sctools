package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by xxx_sharma on 10/16/16.
 */
@XmlRootElement(name="workskills")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({WorkSkill.class})
public class WorkSkills {

    private List<WorkSkill> workskill;
    private String workskillGroup;

    public WorkSkills () {}

    @XmlElement(name="workskill")
    public List<WorkSkill> getWorkskill() {
        return workskill;
    }

    public void setWorkskill(List<WorkSkill> workskill) {
        this.workskill = workskill;
    }

    @XmlElement(name="workskill_group")
    public String getWorkskillGroup() {
        return workskillGroup;
    }

    public void setWorkskillGroup(String workskillGroup) {
        this.workskillGroup = workskillGroup;
    }
}
