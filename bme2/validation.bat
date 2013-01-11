:: made by BME2 TEAM
::@echo off
cls

:: set project path. 
cd D:\prj\bme6\src\bme3\bme2

set TARGET=.\target
set BASE=%TARGET%\bme2-0.1.0.BUILD-SNAPSHOT\WEB-INF
		 
::----------------------------
:: don't have to edit below - unless you have more Application Request Factories

::set OUTPUT=%BASE%\classes
set OUTPUT=%TARGET%\generated-sources\gwt

IF NOT EXIST %TARGET% (
	echo %TARGET% doesn't exists, Please check your directory structure.
	echo Validation file must run from project root directory.
	pause
	exit
)

IF exist %OUTPUT% ( 
	echo %OUTPUT% exists 
) ELSE ( 
	mkdir %OUTPUT% && echo %OUTPUT% created. 
)

set CP=
set CP=%CP%%BASE%\classes;
set CP=%CP%%BASE%\lib\*;
:: '*' will only work with java 1.6+

:: these need to be in there
::CP="%CP%%BASE%/lib/requestfactory-apt.jar:";
::CP="%CP%%BASE%/lib/requestfactory-server+src.jar:";

echo DEBUG ClassPath: %CP%

:: these need to be in there
set PATH=C:\Program Files\Java\jdk1.6.0_32\bin\
::set CLASSPATH=.;C:\Users\tapant\.m2\repository\.

java -cp %CP% com.google.web.bindery.requestfactory.apt.ValidationTool %OUTPUT% medizin.client.factory.request.McAppRequestFactory
:: Do you need more Request Factories???

:: the end
pause
::exit