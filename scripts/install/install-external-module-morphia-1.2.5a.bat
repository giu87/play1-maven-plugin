rem call install-mongo-java-driver-2.6.5a
rem call install-morphia-1.00-20110403

call ..\set-external-modules-home.bat
set MODULE_NAME=morphia
set VERSION=1.2.5a
set JAR_FILE_NAME=play-%MODULE_NAME%-%VERSION%

call install-external-module-with-jar.bat