package com.oracle.ofsc.etadirect.camel.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.oracle.ofsc.etadirect.rest.Recurrence;
import com.oracle.ofsc.etadirect.rest.ResourceJson;
import com.oracle.ofsc.etadirect.rest.RouteInfo;
import com.oracle.ofsc.etadirect.rest.RouteList;
import com.oracle.ofsc.etadirect.rest.WorkSchedule;
import com.oracle.ofsc.etadirect.soap.*;
import com.oracle.ofsc.etadirect.utils.OfscTimeZone;
import com.oracle.ofsc.transforms.GenericResourceData;
import com.oracle.ofsc.transforms.RouteReportData;
import com.oracle.ofsc.transforms.TransportResourceData;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.restlet.data.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides mapping of the current request to the required XML that should be
 * sent as "SOAP" request to server.
 */
@SuppressWarnings("unused")
public class Resource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class.getName());
    private static final String SOAP_WRAPPER_HEADER =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:toatech:ResourceManagement:1.0\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>";

    private static final String SOAP_WRAPPER_FOOTER = "   </soapenv:Body>\n" + "</soapenv:Envelope>";
    private static final boolean USE_MD5 = true;
    private static final ObjectMapper resourceMapper = new ObjectMapper();

    /**
     * Generates body for resource "get" request
     * SOAP call no longer used.
     * @param exchange
     */
    @Deprecated
    public void mapToGetRequest(Exchange exchange) {
        String externalId = (String) exchange.getIn().getHeader("id");

        LOGGER.info("Generate Body For ResourceID: {}", externalId);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock = Security.generateUserAuth((String) exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);

        GetResource getResource = new GetResource();
        getResource.setUser(userBlock);
        getResource.setId(externalId);

        String body = null;
        try {
            JAXBContext context = JAXBContext.newInstance(GetResource.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(getResource, sw);
            body = sw.toString();
            LOGGER.info("Generated Body: \n {}", body);
        } catch (JAXBException e) {
            LOGGER.error("Failed To Marshal Object: {}", e.getMessage());
        }
        StringBuffer sb = new StringBuffer();
        sb.append(SOAP_WRAPPER_HEADER).append(body).append(SOAP_WRAPPER_FOOTER);
        exchange.getIn().setBody(sb.toString());
    }

    /**
     * Handles updates for Resets or Adjustments.  If this is invoked on the Sunday
     * it performs a reset.
     * Any other day of the week will check the total hours, worked and update the
     * values.
     *
     * @param exchange
     */
    public void updateImpactible(Exchange exchange) {

        Map resourceInfo = (Map )exchange.getProperty("employee_info");
        String resourceId = (String) resourceInfo.get("Employee_No");
        Integer impactHours = (Integer )resourceInfo.get("IMPACT_HOURS");
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        String resetForDay  = (String )exchange.getIn().getHeader("routeDay");

        DateTime resetDate = dtf.parseDateTime(resetForDay);
        int dow = resetDate.getDayOfWeek();

        LOGGER.info("Generate Body For Resource Update Resource ID: {} For {} Impact Hours",  resourceId, impactHours);
        HashMap<String, String> authInfo =
                Security.extractURLInfo((String) exchange.getProperty("original_headers"));
        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        String restBody = null;
        if (dow == DateTimeConstants.SUNDAY) {
            restBody = generateImpactibleReset(impactHours, 0);
        }
        else {
            // TODO: Call the adjustment function based on the number of hours worked.
        }

        // Set Values For HTTP4:
        exchange.getIn().setHeader("id", resourceId);
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.setProperty("CamelHttpQuery", exchange.getIn().getHeader("CamelHttpQuery"));
        exchange.getIn().setBody(restBody);

    }
    public void resetSundayShift(Exchange exchange) {
        String resourceId = (String )exchange.getIn().getHeader("id");
        String resetForWeekStarting  = (String )exchange.getIn().getHeader("routeDay");
        String shift = (String )exchange.getProperty("has_sunday_shift");
        LOGGER.info("Processing Schedule For Weekend (Sunday) Continuity Resource ID {} Resetting Week Starting {}", resourceId, resetForWeekStarting);

        HashMap<String, String> authInfo =
                Security.extractURLInfo((String) exchange.getProperty("original_headers"));
        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        // Set Values For HTTP4:
        exchange.getIn().setHeader("id", resourceId);
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        // Must Always Trim As The Data Has /r and control characters in it:
        String restBody = null;
        try {
            restBody = resourceMapper.writeValueAsString(generateWorkScheduleForDay(resetForWeekStarting, StringUtils.trim(shift)));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }
        exchange.getIn().setBody(restBody);
    }

    public void resetSaturdayShift(Exchange exchange) {
        String resourceId = (String )exchange.getIn().getHeader("id");
        String resetForWeekStarting  = (String )exchange.getIn().getHeader("routeDay");
        String shift = (String )exchange.getProperty("has_saturday_shift");
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime saturday = dtf.parseDateTime(resetForWeekStarting).plus(Period.days(6));
        LOGGER.info("Processing Schedule For Weekend (Saturday) Continuity Resource ID {} Resetting Week Starting {}", resourceId, resetForWeekStarting);

        HashMap<String, String> authInfo =
                Security.extractURLInfo((String) exchange.getProperty("original_headers"));
        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        // Set Values For HTTP4:
        exchange.getIn().setHeader("id", resourceId);
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
        // Must Always Trim As The Data Has /r and control characters in it:

        String restBody = null;
        try {
            restBody = resourceMapper.writeValueAsString(generateWorkScheduleForDay(dtf.print(saturday), StringUtils.trim(shift)));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }
        exchange.getIn().setBody(restBody);

    }
    private WorkSchedule generateWorkScheduleForDay(String day, String shift) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormat.forPattern("HH:mm:SS");

        // Parse The Shift
        String[] timerange = StringUtils.split(shift, "-");
        LOGGER.info("Using Start Time: {}, End Time: {} For Shift", timerange[0], timerange[1]);
        // Adjust Times To Clock Hours (note that there are no minutes
        DateTime startTime = convertAmPmTo24Hr(timerange[0]);
        DateTime endTime = convertAmPmTo24Hr(timerange[1]);

        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setRecordType("working");
        workSchedule.setStartDate(day);
        workSchedule.setEndDate(workSchedule.getStartDate());
        workSchedule.setShiftType("regular");
        workSchedule.setWorkTimeStart(tf.print(startTime));
        workSchedule.setWorkTimeEnd(tf.print(endTime));
        Recurrence recurrence = new Recurrence();
        recurrence.setRecurrenceType("daily");
        recurrence.setRecurEvery(1);
        workSchedule.setRecurrence(recurrence);

        return workSchedule;
    }

    private DateTime convertAmPmTo24Hr(String hour) {
        Integer effectiveHour;
        if (StringUtils.endsWithIgnoreCase(hour, "am")) {
            effectiveHour = Integer.parseInt(StringUtils.removeEndIgnoreCase(hour, "am"));
        }
        else {
            Integer hour24 = Integer.parseInt(StringUtils.removeEndIgnoreCase(hour, "pm"));
            if (12 == hour24) {
                effectiveHour = hour24;
            }
            else {
                effectiveHour = hour24 + 12;
            }
        }
        return DateTime.now().withTimeAtStartOfDay().plus(Period.hours(effectiveHour));
    }

    /**
     * For a given day, will set up a 9-5 schedule for the whole week (7days out)
     * Works on the basis of a 7 day work week, starting on Sunday and going to Saturday
     */
    public void resetShiftsForWeek (Exchange exchange) {
        Map resourceInfo = (Map )exchange.getProperty("employee_info");
        String sundayShift = StringUtils.trim((String )resourceInfo.get("CONTY_SUN_SHIFT"));
        String saturdayShift = StringUtils.trim((String )resourceInfo.get("CONTY_SAT_SHIFT"));

        exchange.setProperty("has_sunday_shift", StringUtils.isBlank(sundayShift) ? null : sundayShift);
        exchange.setProperty("has_saturday_shift", StringUtils.isBlank(saturdayShift) ? null : saturdayShift);

        // Verify That The Date Is Sunday
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter tf = DateTimeFormat.forPattern("HH:mm:SS");
        String resetForDay  = (String )exchange.getIn().getHeader("routeDay");
        String resourceId = (String )exchange.getIn().getHeader("id");
        Integer continuity_hours = (Integer )resourceInfo.get("POSITION_HRS");
        // Compute The Hours Worked For The Schedule
        int hoursPerDay = Math.round(continuity_hours / 5);
        LOGGER.info("Resource Can Work {} Hours Per Day", hoursPerDay);

        DateTime resetDate = dtf.parseDateTime(resetForDay);
        Preconditions.checkArgument(DateTimeConstants.SUNDAY == resetDate.getDayOfWeek(), "Reset Must Be Done For Sunday");

        // Build The Message For Calendar Assignment
        LOGGER.info("Generate Body For Resource Schedule Reset Resource ID: {} On {}",  resourceId, resetForDay);
        HashMap<String, String> authInfo =
                Security.extractURLInfo((String) exchange.getProperty("original_headers"));
        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd =   authInfo.get("passwd");

        WorkSchedule workSchedule = new WorkSchedule();
        DateTime startTime = new DateTime(2017, 01, 01, 9, 0, 0);
        workSchedule.setRecordType("working");
        workSchedule.setStartDate(dtf.print(resetDate.plus(Period.days(1))));
        int dayOffset = 5;
        workSchedule.setEndDate(dtf.print(resetDate.plus(Period.days(dayOffset))));
        workSchedule.setShiftType("regular");
        workSchedule.setWorkTimeStart(tf.print(startTime));
        workSchedule.setWorkTimeEnd(tf.print(startTime.plus(Period.hours(hoursPerDay))));
        Recurrence recurrence = new Recurrence();
        recurrence.setRecurrenceType("daily");
        recurrence.setRecurEvery(1);
        workSchedule.setRecurrence(recurrence);

        // Set Values For HTTP4:
        exchange.getIn().setHeader("id", resourceId);
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");

        String restBody = null;
        try {
            restBody = resourceMapper.writeValueAsString(workSchedule);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed To Marshal JSON Object: {}", e.getMessage());
        }
        exchange.getIn().setBody(restBody);

    }

    /**
     * evaluates if the response from the OFSC for a Get Resource was
     * successful or not.
     * Populates the Exchange Header: ofsc_resource_exists
     *
     * @param exchange
     */
    public void checkOfscResponse(Exchange exchange) {
        org.restlet.engine.adapter.HttpResponse response =
                (org.restlet.engine.adapter.HttpResponse) exchange.getIn().getHeader("CamelRestletResponse");

        ResourceJson resource = (ResourceJson )exchange.getIn().getBody();
        if (response != null && response.getStatus().equals(Status.SUCCESS_OK)) {
          if (resource.getStatus() != null && resource.getStatus().equals("active")) {
              LOGGER.warn("Active Resource Found In OFSC - Processing Reset");
              exchange.getIn().setHeader("ofsc_resource_exists", true);
          }
            else {
              LOGGER.warn("Failed To Find Resource In OFSC - Resource Is Not Active");
              exchange.getIn().setHeader("ofsc_resource_exists", false);
          }
        }
        else {
            LOGGER.warn("Failed To Find Resource In OFSC - Request Failed");
            exchange.getIn().setHeader("ofsc_resource_exists", false);
        }
    }

    private int getEndOfWeekDays(DateTime resetDate) {
        int dow = resetDate.getDayOfWeek();
        return DateTimeConstants.SATURDAY - dow;
    }

    private String generateImpactibleReset(Integer allowableImpactHours, Integer workedImpactHours) {

        if (allowableImpactHours == 0) {
            return "{"
                    + " \"XA_IMPACTABLE\": \"0\", "
                    + " \"impact_hours\": 0, "
                    + " \"impact_worked\": 0"
                    + "\n}";
        }
        else {
            return "{\n"
                    + " \"XA_IMPACTABLE\": \"1\",\n"
                    + " \"impact_hours\": " + allowableImpactHours + ", "
                    + " \"impact_worked\": 0"
                    + "\n}";
        }
    }

    public void mapToInsertUser(Exchange exchange) {
        String category = (String) exchange.getIn().getHeader("resource_category");

        LOGGER.info("Generate Insert User Body");
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock = Security.generateUserAuth((String) exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);
        InsertUser insertUser = null;

        switch (category) {
        case "generic":
            insertUser = this.generateGenericUser((GenericResourceData) exchange.getIn().getBody());
            break;

        default:
        }

        // Load The Credentials
        insertUser.setUser(userBlock);
        String soapBody = null;
        try {
            JAXBContext context = JAXBContext.newInstance(InsertUser.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(insertUser, sw);
            soapBody = sw.toString();
        } catch (JAXBException e) {
            LOGGER.error("Failed To Marshal Object: {}", e.getMessage());
        }
        StringBuffer sb = new StringBuffer();
        sb.append(SOAP_WRAPPER_HEADER).append(soapBody).append(SOAP_WRAPPER_FOOTER);
        exchange.getIn().setBody(sb.toString());
    }

    /**
     * Generates the request body and complete SOAP request for a resource creation
     */
    public void mapToInsertResource(Exchange exchange) {
        String id = (String) exchange.getIn().getHeader("id");
        String category = (String) exchange.getIn().getHeader("resource_category");

        LOGGER.info("Generate Insert Resource Body For Insertion Under ResourceID: {}", id);
        // TODO: The request should have the information for the request, however, this is hardcoded for now:
        User userBlock = Security.generateUserAuth((String) exchange.getIn().getHeader("CamelHttpQuery"), !USE_MD5);
        InsertResource insertResource = null;

        switch (category) {
        case "transportation":
            insertResource = this.generateTransportationResource(id, (TransportResourceData) exchange.getIn().getBody());
            break;
        case "generic":
            insertResource = this.generateGenericResource(id, (GenericResourceData) exchange.getIn().getBody());
            break;
        default:

        }

        // Load The Credentials
        insertResource.setUser(userBlock);

        String soapBody = null;
        try {
            JAXBContext context = JAXBContext.newInstance(InsertResource.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(insertResource, sw);
            soapBody = sw.toString();
        } catch (JAXBException e) {
            LOGGER.error("Failed To Marshal Object: {}", e.getMessage());
        }

        StringBuffer sb = new StringBuffer();
        sb.append(SOAP_WRAPPER_HEADER).append(soapBody).append(SOAP_WRAPPER_FOOTER);
        exchange.getIn().setBody(sb.toString());
    }

    private InsertUser generateGenericUser(GenericResourceData user) {
        InsertUser insertUser = new InsertUser();

        insertUser.setLogin(user.getLogin());
        List<Property> propertyList = new ArrayList<>(5);
        propertyList.add(new Property("status", "active"));
        try {
            propertyList.add(new Property("password", Security.hexMD5Encode(user.getPass())));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed To MD5 (Hex) Encrypt Password");
        }
        propertyList.add(new Property("name", user.getName()));
        propertyList.add(new Property("language", "en"));
        propertyList.add(new Property("type", "Technician"));
        propertyList.add(new Property("main_resource_id", user.getResourceId()));
        // Must map from time zone DB values to Ofsc specific values
        try {
            OfscTimeZone tz = OfscTimeZone.valueOf(StringUtils.substringAfter(user.getTimezone(), "/"));
            propertyList.add(new Property("time_zone", tz.getOfscLabel()));
        } catch (Exception e) {
            LOGGER.error("Unknown TimeZone {}", user.getTimezone());
            propertyList.add(new Property("time_zone", "Eastern"));
        }
        Properties properties = new Properties();
        properties.setProperty(propertyList);
        insertUser.setProperties(properties);

        com.oracle.ofsc.etadirect.soap.Resource resource = new com.oracle.ofsc.etadirect.soap.Resource();
        resource.setValues(ImmutableList.of(user.getResourceId()));
        insertUser.setResources(resource);
        return insertUser;
    }

    private InsertResource generateGenericResource(String id, GenericResourceData resource) {
        InsertResource insertResource = new InsertResource();
        insertResource.setId(resource.getResourceId());

        // Mandatory Elements
        ArrayList<Property> properties = new ArrayList<>(10);
        insertResource.setProperties(properties);
        properties.add(new Property("status", "active"));
        properties.add(new Property("parent_id", id));
        properties.add(new Property("name", StringUtils.trim(resource.getName())));
        properties.add(new Property("language", "en"));
        properties.add(new Property("type", "PR"));
        properties.add(new Property("Resource type", "2"));
        // Must map from time zone DB values to Ofsc specific values
        try {
            OfscTimeZone tz = OfscTimeZone.valueOf(StringUtils.substringAfter(resource.getTimezone(), "/"));
            properties.add(new Property("time_zone", tz.getOfscLabel()));
        } catch (Exception e) {
            LOGGER.error("Unknown TimeZone {}", resource.getTimezone());
            properties.add(new Property("time_zone", "Eastern"));
        }

        if (StringUtils.isBlank(resource.getAffiliation())) {
            properties.add(new Property("resource_affiliation", "None"));
        }
        else {
            properties.add(new Property("resource_affiliation", resource.getAffiliation()));
        }
        properties.add(new Property("work_hours", Integer.toString(resource.getWeeklyHours())));

        // Need To Parse The WorkSkill Field
        WorkSkills workSkills = new WorkSkills();
        ArrayList<WorkSkill> workskillList = new ArrayList<>(5);
        String workSkillsVec = resource.getWorkSkillList();
        String skills[] = StringUtils.split(workSkillsVec, "|");
        for (String skill : skills) {
            workskillList.add(new WorkSkill(skill, "100"));
        }

        workSkills.setWorkskill(workskillList);
        insertResource.setWorkskills(workSkills);

        return insertResource;
    }

    /**
     * Specific mapping for the Transportation Resource (Trucks)
     * @param id
     * @param truck
     * @return
     */
    private InsertResource generateTransportationResource(String id, TransportResourceData truck) {

        InsertResource insertResource = new InsertResource();
        insertResource.setId(truck.getName());

        // Mandatory Elements
        ArrayList<Property> properties = new ArrayList<>(10);
        insertResource.setProperties(properties);
        properties.add(new Property("status", "active"));
        properties.add(new Property("parent_id", id));
        properties.add(new Property("name", truck.getName()));
        properties.add(new Property("language", "en"));
        properties.add(new Property("time_zone", truck.getTimezone()));
        properties.add(new Property("weight_cap", truck.getWeight()));
        properties.add(new Property("cubic_cap", truck.getCubeCap()));
        // Overridden Later If This Is A Lift Gate Truck
        properties.add(new Property("type", "PR"));

        // Look For Any Work Skills

        WorkSkills workSkills = new WorkSkills();
        // Don't Set Any Groups
        // workSkills.setWorkskillGroup("TruckingGroup");

        ArrayList<WorkSkill> workskill = new ArrayList<>(5);
        workskill.add(new WorkSkill("LVL1", "100"));
        if (StringUtils.isNotBlank(truck.getLiftGate()) && truck.getLiftGate().equalsIgnoreCase("y")) {
            // Only Add LiftGate If We Have Y In The Data
            workskill.add(new WorkSkill("LVL2", "100"));
        }

        workSkills.setWorkskill(workskill);
        insertResource.setWorkskills(workSkills);
        properties.add(new Property("type", "LGT"));

        return insertResource;
    }

    public void authOnly(Exchange exchange) {
        String bucketId = (String) exchange.getIn().getHeader("id");
        LOGGER.info("Generate Auth Only For ResourceId: {}", bucketId);

        HashMap<String, String> authInfo = Security.extractURLInfo((String) exchange.getIn().getHeader("CamelHttpQuery"));

        String username = authInfo.get("user") + "@" + authInfo.get("company");
        String passwd = authInfo.get("passwd");
        // Set Values For HTTP4:
        exchange.getIn().setHeader("username", username);
        exchange.getIn().setHeader("passwd", passwd);
        LOGGER.info("SetUp Completed For User: " + exchange.getIn().getHeader("username"));

    }

    public void extractRoutes(Exchange exchange) {
        LOGGER.info("Generating CSV Routing Output From Route Results");
        InputStream is = (InputStream )exchange.getIn().getBody();
        // Marshal Json IS To Object
        RouteList routeList;
        try {
            routeList = resourceMapper.readValue(is, RouteList.class);
            LOGGER.info("Completed Server Route List Parsing");
        } catch (IOException e) {
            LOGGER.error("Failed To Parse Server Response Json (InputStream)", e);
            return;
        }

        List<RouteReportData> resultList = new ArrayList<>(10);
        // Bail out if there is nothing to parse.
        if (routeList.getItems() == null) {
            LOGGER.warn("No Information For Route!");
        }
        else {
            // Generate the output based on the object Map To Bindy:
            for (RouteInfo info : routeList.getItems()) {
                RouteReportData report = new RouteReportData();
                report.setActivityId(info.getActivityId());
                report.setApptNumber(info.getApptNumber());
                report.setResourceId(info.getResourceId());
                report.setDate(info.getDate());
                report.setStartTime(info.getStartTime());
                report.setEndTime(info.getEndTime());
                report.setStatus(info.getStatus());
                report.setTimezone(info.getResourceTimeZone());
                if (null != info.getTravelTime()) {
                    report.setTravelTime(info.getTravelTime());
                }
                if (null != info.getDuration()) {
                    report.setDuration(info.getDuration());
                }
                if (null != info.getPositionInRoute()) {
                    report.setRoutePosition(info.getPositionInRoute());
                }
                if (null != info.getLatitude()) {
                    report.setLatitude(info.getLatitude());
                }
                if (null != info.getLongitude()) {
                    report.setLongitude(info.getLongitude());
                }
                resultList.add(report);
            }
        }
        exchange.getIn().setBody(resultList);
    }
}

