# networkScanner
This project should be showing, how to write an simple network scanner by using JavaFX.

Here we used the Launch4j Framework to wrap the executable JAR File in a EXE File, to make it possible to execute this application under Windows OS by double click.

## Build and Deploy steps:
* First make sure you have stored a custom JDK Version 18.0.1.1 in the project base directory with name `customJDK-18.0.1.1`, otherwise use Java jlink command line tool to build an customize JDK for this application. To do that, follow the steps in the jlink text file in the project base dir
* If you have succesfully build a customize JDK Version 18.0.1.1, store them with the specific name (below) in the project base directory
* Next you can build and deploy this application in one step by using Maven with 'mvn clean package' command
* After successfully Maven build process, unzip the 'Simple-Network-Scanner.zip' archiv at the target folder and run EXE -> have fun :blush:

To understand how we can build and deploy JavaFX application by using Maven, please check out my POM.

I hope this little instruction was helpful. Enjoy and happy coding.

## The network scan details:
* used subnet mask:             `255.255.255.0` (CIDR-Suffix /24)
* supported IP-Protocol:        `IPv4`
* scan works on:                `OSI layer 3/4`
* used protocols:               `ICMP/TCP`

## Used JDK
* OpenJDK                       Version 18.0.1.1

## Used Frameworks
* OpenJfx                       Version 18.0.1
* Log4J                         Version 2.17.2
* Launch4j                      Version 2.1.2

## Used Build Tool
* Apache Maven                  Version 3.8.5
