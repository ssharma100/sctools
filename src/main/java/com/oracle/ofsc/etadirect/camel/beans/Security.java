package com.oracle.ofsc.etadirect.camel.beans;

import com.google.common.base.Preconditions;
import com.oracle.ofsc.etadirect.soap.User;

/**
 * Created by Samir on 10/6/2016.
 */
public class Security {

    /**
     * Static class - do not instantiate
     */
    private Security() {}

    /**
     * Generates a JAXB object for the user block in the ETAdirect SOAP request
     * @param company
     * @param user
     * @param password
     * @return
     */
    public static User generateUserAuth(String company, String user, String password) {
        Preconditions.checkNotNull(company, "Must Provide 'company' For Auth Credentials");
        Preconditions.checkNotNull(user, "Must Provide 'user' For Auth Credentials");
        Preconditions.checkNotNull(password, "Must Provide 'password' For Auth Credentials");

        User userBlock = new User();
        return userBlock;
    }

}
