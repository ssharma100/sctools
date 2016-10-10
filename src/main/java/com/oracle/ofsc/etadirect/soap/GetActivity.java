package com.oracle.ofsc.etadirect.soap;

import javax.xml.bind.annotation.*;

/**
 * Created by Samir on 10/9/2016.
 */
@XmlRootElement(name="get_activity")
@XmlType(propOrder = { "user", "activity_id" })
@XmlSeeAlso(User.class)
public class GetActivity {

    private User user;
    private String activity_id;

    @XmlElement(name="user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @XmlElement(name="activity_id")
    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }
}
