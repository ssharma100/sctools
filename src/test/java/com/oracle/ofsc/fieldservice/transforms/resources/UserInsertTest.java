package com.oracle.ofsc.fieldservice.transforms.resources;

import com.google.common.collect.ImmutableList;
import com.oracle.ofsc.etadirect.soap.Properties;
import com.oracle.ofsc.etadirect.soap.Resource;
import com.oracle.ofsc.etadirect.soap.InsertUser;
import com.oracle.ofsc.etadirect.soap.Property;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 *
 */
@RunWith(JUnit4.class)
public class UserInsertTest {

    @Test public void testUserInsertSOAPTransform() throws JAXBException {

        // Build Our User_Insert Object
        InsertUser insertUser = new InsertUser();
        insertUser.setLogin("login-user");
        Resource resource = new Resource(ImmutableList.of("resource1", "resource2"));
        insertUser.setResources(resource);

        Property firstProp = new Property("nameA", "valueA");
        Property secondProp = new Property("nameB", "valueB");
        Properties properties = new Properties();
        properties.setProperty(ImmutableList.of(firstProp, secondProp));

        insertUser.setProperties(properties);

        String soapBody;

        JAXBContext context = JAXBContext.newInstance(com.oracle.ofsc.etadirect.soap.InsertUser.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter sw = new StringWriter();
        marshaller.marshal(insertUser, sw);
        soapBody = sw.toString();

        System.out.println("SOAP Fragment:\n" + soapBody);
    }
}
