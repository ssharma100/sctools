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

The loader will take the input file of the format, depending on what loader you are using (loader must match format of uploaded data)

http://localhost:8085/sctool/v1/generic/resource/{id}
Where {id} is the external resource ID of the parent under which the new resource will be added.


_Location Loading_
http://localhost:8085/sctool/v1/location?user=SOAPUPLOAD&company=securitaselectro1.test&passwd=Test123

### Generating A Route (Customer Specific)
The system is capable of generating the route plan for a given use. This takes the list of appointments for a given 
day and converts the route to a DB table.  The DB table can then be used to integrate to other systems and/or provide
reporting and analytic information on the route.
The application takes a reosource_id and date, and looks for all jobs that were scheduled for the given day.  For the day,
the jobs are copied to the route_plan table and given a sequence.
The route table also provides for integration with Google, whereby we can populate drive time and address information
(derived from Latitude/Longitude).

