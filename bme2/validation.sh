#!/bin/sh
# made by BME2 TEAM

# Project Path to Web-inf folder (with no trailing /)
TARGET="./target";
BASE="$TARGET/bme-0.1.0.BUILD-SNAPSHOT/WEB-INF";


if [ ! -d "$TARGET" ]; then
	echo "$TARGET doesn't exists, Please check your directory structure.";
	echo "Validation file must run from project root directory.";
	read -p "Press [Enter] key to continue..."
	exit
if

#----------------------------
# don't have to edit below - unless you have more Application Request Factories

OUTPUT="$TARGET/generated-sources/gwt";
if [ ! -d "$DIRECTORY" ]; then
    # Control will enter here if $DIRECTORY doesn't exist.
  mkdir -p "$OUTPUT";
fi

CP="";
CP="$CP$BASE/classes:";
CP="$CP$BASE/lib/*:"; # '*' will only work with java 1.6+

# these need to be in there
#CP="$CP./lib/requestfactory-apt.jar:";
#CP="$CP./lib/requestfactory-server+src.jar:";

echo "DEBUG ClassPath: $CP";

java -cp $CP com.google.web.bindery.requestfactory.apt.ValidationTool $OUTPUT \
  medizin.client.a_nonroo.app.request.McAppRequestFactory \
  medizin.client.managed.request.ApplicationRequestFactory \
  medizin.shared.scaffold.ScaffoldRequestFactory
  # Do you need more Request Factories???
  

# the end
read -p "Press [Enter] key to continue..."
exit;
