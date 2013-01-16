Required Tools
==============

GWT 2.5.0 <br/>
Spring Roo 1.2.3 <br/>
SpringSource Tool Suite 2.9.1.RELEASE <br/>

Required Spring Roo commands
=============================

When you create new domain class then execute following two commands : 

web gwt proxy all --package ~.client.proxy

web gwt request all --package ~.client.request


GWT Validation (Only for Hosted Mode)
=====================================

When you change any method signature or add new method on domain or corresponding request class then execute "validation.bat/.sh" and refresh the project.

Note : To run validation.bat from sts uncomment "cd D:\prj\bme6\src\bme3\bme2" and change the project path to your path.
