robocode-ctf-orthogonobot
=====================

A competitive robocode robot using the Maven build system.  To install this package:

1. Install robocode
-------------------

Install both the robocode basic distribution and the robocode testing plugin.  Both of these setup.jar files are available for version 1.7.4.4 [here](https://sourceforge.net/projects/robocode/files/robocode/1.7.4.4/).  Do NOT download version 1.8.0.

Be sure to run the system manually from the command line to ensure that you have the appropriate Java installed and so forth. 

2. Install Maven
----------------

Start by following the [directions on installing Maven](http://maven.apache.org/download.cgi).

Be sure to run mvn --version to verify that it is correctly installed.  This package has been tested using Maven 3.0.4.

3. Download this robocode-ctf-orthogonobot package
----------------------------------------------

Clone this repository.

4. Install robocode jar files into your local Maven repository
--------------------------------------------------------------

Robocode binaries are not provided as part of public Maven repositories, so the next step is to install the seven jar files needed for compilation and testing into your local repository.  You accomplish this by changing directory to robocode/libs and executing the following seven commands:

```
mvn install:install-file -Dfile=robocode.jar -DartifactId=robocode  -DgroupId=net.sourceforge.robocode -Dversion=1.7.4.4 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=robocode.testing.jar -DartifactId=robocode.testing -DgroupId=net.sourceforge.robocode  -Dversion=1.7.4.4 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=robocode.battle-1.7.4.4.jar -DartifactId=robocode.battle -DgroupId=net.sourceforge.robocode -Dversion=1.7.4.4 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=robocode.core-1.7.4.4.jar   -DartifactId=robocode.core   -DgroupId=net.sourceforge.robocode -Dversion=1.7.4.4 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=robocode.host-1.7.4.4.jar   -DartifactId=robocode.host   -DgroupId=net.sourceforge.robocode -Dversion=1.7.4.4 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=robocode.repository-1.7.4.4.jar -DartifactId=robocode.repository   -DgroupId=net.sourceforge.robocode -Dversion=1.7.4.4 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=picocontainer-2.14.2.jar -DgroupId=net.sourceforge.robocode -DartifactId=picocontainer -Dversion=2.14.2 -Dpackaging=jar -DgeneratePom=true
```

A typical output from one of these commands might be:

```
[~/robocode/libs]-> mvn install:install-file -Dfile=robocode.jar -DartifactId=robocode -DgroupId=net.sourceforge.robocode -Dversion=1.7.4.4 -Dpackaging=jar -DgeneratePom=true
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-install-plugin:2.3.1:install-file (default-cli) @ standalone-pom ---
[INFO] Installing /Users/johnson/robocode/libs/robocode.jar to /Users/johnson/.m2/repository/net/sourceforge/robocode/robocode/1.7.4.4/robocode-1.7.4.4.jar
[INFO] Installing /var/folders/__/qq1ydtj56n3fxtccjh9k6dk80000gn/T/mvninstall1027288217865601684.pom to /Users/johnson/.m2/repository/net/sourceforge/robocode/robocode/1.7.4.4/robocode-1.7.4.4.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.454s
[INFO] Finished at: Mon Jan 28 14:28:31 HST 2013
[INFO] Final Memory: 7M/309M
[INFO] ------------------------------------------------------------------------
[~/robocode/libs]-> 
```
5.  Build and test the system
-----------------------------

Now that everything is installed, build and test the system. You use the standard Maven 'test' target.  There are two special aspects of this process of which you should be aware.  

First, the RobotTestBed class and the Maven POM file requires the definition of a System Property called "robocode.home", which should point to the directory where Robocode is installed. To define this property and its value, use the -Drobocode.home=robocodeHomeDirectory command line option.

Second, the Robocode runtime system needs your newly developed robot to be known to the system during testing.  To accomplish this, the pom.xml file defines a "copy-resource" goal that copies your robot binary from the target/classes directory to the robocode.home/robots directory after completing compilation.

Note that this approach does not remove these files from the robocode installation after the testing process is done. 

6.  Install robocode-ctf-orthogonobot into Eclipse
----------------------------------------------

Now that the system is running from the command line, you'll want to also run it from Eclipse.  To do so, bring up Eclipse, and select File | Import | Maven | Existing Maven Projects, and then complete the dialog boxes to import your project.  Eclipse will read the POM file in order to determine the libraries to include on the build path.  

To run the ctf.orthogonobot robot within Eclipse, you must configure Eclipse and Robocode in the normal way:
  * In the Run configuration, change the working directory to your Robocode installation directory. 
  * In the Robocode window, select Options | Preferences | Development Options to add the target/classes directory so that Robocode will see your robot.

To run the test cases, edit the Run configuration for each test to include -Drobocode.home=robocodeHomeDirectory as a VM argument. 




