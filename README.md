# OFSC Integration Tools (OFSC Tools)
Solution Consultant Application Tooling

The application is built using Apache Camel (OSS) that provides an integration and tooling framework that allows for the
upload, query and management of activities, resources and users in the OFSC platform.
The tool is customizable - if you are familiar with Java and Camel it should be a matter of creation your own branch
off the GitHub and making the modifications as needed to the transformations.

# Installation And Set-Up
There are  few things that you will need to get started (even if you don't edit any code):

* Java SDK (if you want to develop/edit) or JRE Otherwise.  Must use Java 8.
* Maven

It helps to install the components in the order as shown in the following sections.

## Java 8 SDK Installation
You can find the JDK/SDK Download at [Oracle's JDK Page](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
Download the version that best fits your system/OS and install it - the directions should be on the site and are
specific to the type of platform you are running on.
Based on your installation you should make note of the JAVA_HOME environment variable (make sure that it has been
set and points to the Java 8 version that you just installed).

Also run __java -version__
This should tell you that you are running a Java 8 version

## Maven Installation
Please ensure that you install a current version of Maven (at least 3.0 or above).
You can find the latest Maven version at [Maven Download page](https://maven.apache.org/download.cgi)
The same Maven download page also has [installation directions](https://maven.apache.org/install.html)

To run this router either embed the jar inside Spring
or to run the route from within maven try

    mvn camel:run

For more help see the Apache Camel documentation.

## Using The RESTful Calls
To provide the best methodology of invocation

    http://camel.apache.org/

## Example Requests

### Common Elements

### Loading A Resource:
Loading of resource supports:
- Generic Resources: A normal/basic resource
- Truck/Transportation Based Resource - resources that have weight and cargo capacity

The loader will take the input file of the format, depending on what loader you are using (loader must match format of uploaded data), POST to:

    http://localhost:8085/sctool/v1/generic/resource/{id}

Where {id} is the external resource ID of the parent (folder or group) under which the new resource will be added.
THe format for the input body should be:

    resourceId,name,email,phone,resourceInternalId,parentResourceInternalId,resourceType,timeZone,dateFormat,timeFormat,personnel_type
    
Note: The 19x version of OFSC requires that a Resource and a User be created in order to use/route to a resource.  If the User is not created the Resource will appear 
as disabled.

### Loading A User:
The latest version of the scheduler need a valid user to be added to each resource in order for the resource to be considered in routing.
 Performing a POST to the upload path for the users is:
 
    http://localhost:8085/sctool/v1/generic/user

This will take the same formatted CSV input as the resource loader (the user information is already included in each line):

    Name, ResourceID, WorkSkillList, Latitude, Longitude, Address, City, State, Zip, TIME_ZONE, HoursPerWeek, Affiliation, Login, Password
    Handy, Douglas, A, 992200220, continuity, 28.3088, -81.4193, 1938 Cattleya Dr, Kissimmee, Florida, 34741-3124, America/New_York, 33, Walmart Purple Consortium, acosta_992200220, test123

_Location Loading_
http://localhost:8085/sctool/v1/location?user=SOAPUPLOAD&company=securitaselectro1.test&passwd=Test123


_Reset Of Schedules_
http://localhost:8085/sctool/v1/acosta/schedule/continuity/reset/{weekstart_date}

### Generating A Route (Customer Specific)
The system is capable of generating the route plan for a given use. This takes the list of appointments for a given 
day and converts the route to a DB table.  The DB table can then be used to integrate to other systems and/or provide
reporting and analytic information on the route.
The application takes a resource_id and date, and looks for all jobs that were scheduled for the given day.  For the day,
the jobs are copied to the route_plan table and given a sequence.
The route table also provides for integration with Google, whereby we can populate drive time and address information
(derived from Latitude/Longitude).

To invoke the call, you need a list the has <route_day>,<resource_id>
With this list, you can feed it in the Postman "Runner", where the Runner will read the file, and parse the CSV according
 to the first line, and substituting the calls to the Tool with the date and resource id.  The typical manual invocation is:
 
 For Impact Jobs:
 http://localhost:8085/sctool/v1/acosta/route/impact/baseline/{route_date}/{resource_id}
 
 For Continuity Jobs:
 http://localhost:8085/sctool/v1/acosta/route/conty/baseline/{route_date}/{resource_id}
 
 ### Adding Activities
 The system will support the upload of activities.  The activity can be setup for the day of the activity or be SLA based.  To change
 this provide a SLA=true or SLA=false on the query path of the URL.  Note that not providing any SLA query parameter will default
 the loader to use NO SLA and create a "due day" activity. 
 
 #### Example: Loading Impact Activities:
 The format for the impact activities is:
 
 \# ActivityKey, ResourceId, ActivityType, StartDate, EndDate, Latitude, Longitude, Duration, StartTime, EndTime, Store, City, State, Zip, Timezone, TimeSlot, Resource_No, DOW
 
 ImpB_A000-1895-4396C889, 992309862, imp330, 2017-08-22, 2017-08-22, 26.0523, -80.1384, 330, 07:00:00, 12:30:00, Winn Dixie 354, Dania, Florida, 33004, Eastern, S17, 1, 1|1|1|1|1|0|0
 
 ImpB_A000-1895-4422C953, 992328945, imp330, 2017-08-15, 2017-08-15, 25.7502, -80.2460, 330, 07:00:00, 12:30:00, Winn Dixie 251, Miami, Florida, 33145, Eastern, S17, 1, 1|1|1|1|1|0|0
 
 This content/file (raw data) needs to be sent to the application end point in order to load the rows (the request is done via POST).
 When making this request the application will run through the provided data (the first line is skipped as the header).
 
