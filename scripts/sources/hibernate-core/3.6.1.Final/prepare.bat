set SRC_DIR=core.patched-play-1.2
xcopy /I /E /Q core %SRC_DIR%
copy ..\..\..\poms\play\hibernate-core-3.6.1.Final-patched-play-1.2.pom %SRC_DIR%\pom.xml
xcopy /I /E /Q /Y patched-files-1.2 %SRC_DIR%
call mvn clean source:jar javadoc:jar --file %SRC_DIR%/pom.xml
