# SSO Custom SAP Data to Cache SPI

Custom SAP Data to Cache SPI.  Makes a request to an external API to be put to a Red Hat Data Grid Cache.


=== Building the Project

Build the project using the following maven command.  The project has been configured so that the code deploy pipeline
can build the project and the output will be put in a directory that is accessible for to the Docker build command.  You must
specify the "directory" parameter to the mvn command.  The following is an example:


    mvn -Ddirectory=/path_to/put/target_directory/ -f /path_to/sap_to_cache_spi/pom.xml clean package


This will compile a jar file to the "target" directory in the path that was specified with the -D switch.  For example:

    /path_to/put/target_directory/target/sap-to-cache-jpa.jar







