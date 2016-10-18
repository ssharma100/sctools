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
Download the vesion that best fits your system/OS and install it - the directions should be on the site and are
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

For more help see the Apache Camel documentation

    http://camel.apache.org/
    