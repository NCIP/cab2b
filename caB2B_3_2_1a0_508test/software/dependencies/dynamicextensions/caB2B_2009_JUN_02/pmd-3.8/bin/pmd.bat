@echo off
rem set FILE=%1%
rem set FORMAT=%2%
rem set RULESETFILES=%3%
java -cp %CLASSPATH%;..\lib\pmd-3.8.jar;..\lib\jaxen-1.1-beta-10.jar;..\lib\jakarta-oro-2.0.8.jar net.sourceforge.pmd.PMD %*
