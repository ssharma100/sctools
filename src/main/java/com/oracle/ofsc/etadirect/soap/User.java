package com.oracle.ofsc.etadirect.soap;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlType;

/**
 * Soap Serializable Class uses for the <user></user>
 * block of the authentication part of the SOAP API request.
 * Can be in any order of fields.
 */
public class User {
    private String now;
    private String login;
    private String company;
    private String auth_string;

    public User() {}

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAuth_string() {
        return auth_string;
    }

    public void setAuth_string(String auth_string) {
        this.auth_string = auth_string;
    }
}
