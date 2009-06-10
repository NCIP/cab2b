Update "cab2bBuildLogger.jar\Mail.properties" with appropriate values 
Update  build.properties with appropriate values.

Copy build.xml and build.properties to some directory (BUILD_HOME)

copy following to ANT_HOME\lib directory
cab2bBuildLogger.jar
mail.jar
activation.jar

From command prompt go to directory BUILD_HOME and type following command 
ant nightly.build 
