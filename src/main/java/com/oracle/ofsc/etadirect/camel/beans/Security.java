package com.oracle.ofsc.etadirect.camel.beans;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.oracle.ofsc.etadirect.soap.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by Samir on 10/6/2016.
 */
public class Security {
    private static final Logger LOGGER = LoggerFactory.getLogger(Security.class.getName());
    /**
     * Static class - do not instantiate
     */
    private Security() {}

    /**
     * Generates a JAXB object for the user block in the ETAdirect SOAP request
     * @param queryStr
     * @return
     */
    public static User generateUserAuth(String queryStr, boolean useMD5) {

        Preconditions.checkNotNull(queryStr, "Query String Must Be Provided For Auth");
        List<String> params = Lists.newArrayList(Splitter.on('&').trimResults().omitEmptyStrings().split(queryStr));

        String user=null;
        String password=null;
        String company=null;

        for (String param : params) {
            String[] nvpair = param.split("=", 2);
            switch (nvpair[0]) {
            case "user":
                user = nvpair[1];
                break;
            case "company":
                company = nvpair[1];
                break;
            case "passwd":
                password = nvpair[1];
                break;
            }
        }
        Preconditions.checkNotNull(company, "Must Provide 'company' For Auth Credentials");
        Preconditions.checkNotNull(user, "Must Provide 'user' For Auth Credentials");
        Preconditions.checkNotNull(password, "Must Provide 'password' For Auth Credentials");

        // Get current time in ISO 8601 Format:
        DateTime currentTime = new DateTime(DateTimeZone.UTC);
        DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
        String now = fmt.print(currentTime);
        LOGGER.info("Using Current Time Stamp Of: {}", now);

        // Compute The Auth String
        String auth = null;
        try {
            if (useMD5) {
                String md5Passwd = hexMD5Encode(password);
                auth = hexMD5Encode(now + md5Passwd);
                LOGGER.info("Auth String (MD5): {}", auth);
            }
            else {
                String loginHash = hexSHA256Encode(user);
                LOGGER.debug("Login Hash: {}" , loginHash);
                String passHash = hexSHA256Encode(password + loginHash);
                LOGGER.debug("Password Hash: {}", passHash);
                auth = hexSHA256Encode(now + passHash);
                LOGGER.info("Auth String (SHA256): {}", auth);
            }

        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed To Generate SHA256 Hash In Authentication Block");
        }

        User userBlock = new User();
        // Format the user block as per auth structure/requirements
        userBlock.setCompany(company);
        userBlock.setLogin(user);
        userBlock.setNow(now);
        userBlock.setAuth_string(auth);
        return userBlock;
    }

    private static String hexSHA256Encode(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] hashData = md.digest();
        LOGGER.debug("Perform SHA256 On: {}", input);
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<hashData.length;i++) {
            String hex=Integer.toHexString(0xff & hashData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String hexMD5Encode( String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes(StandardCharsets.UTF_8));

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
