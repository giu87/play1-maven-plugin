set VERSION=1.5.0
call ..\set-play-home-%VERSION%.bat

set MODULE_NAME=docviewer

call deploy-play-module-with-jar.bat
