# OFSC (TOA) Integration Tools (OFSC Tools)
Solution Consultant Application Tooling

The application is built using Apache Camel (OSS) that provides an integration and tooling framework that allows for the
upload, query and management of activities, resources and users in the OFSC platform.
The tool is customizable - if you are familiar with Java and Camel it should be a matter of creation your own branch
off the GitHub and making the modifications as needed to the transformations.

# Installation And Set-Up
There are  few things that you will need to get started (even if you don't edit any code):

* Maven
* Java SDK (if you want to develop/edit) or JRE Otherwise.  Must use Java 8.

To run this router either embed the jar inside Spring
or to run the route from within maven try

    mvn camel:run

For more help see the Apache Camel documentation

    http://camel.apache.org/
    