set VERSION=1.3.2
call ..\set-play-home-%VERSION%.bat

set MODULE_NAME=secure

call deploy-play-module-without-jar.bat
