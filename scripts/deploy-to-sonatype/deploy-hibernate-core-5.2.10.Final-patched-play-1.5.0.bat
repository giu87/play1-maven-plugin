call ..\set-play-home-1.5.0.bat

set GROUP_ID_PREFIX=com.google.code.maven-play-plugin.
set GROUP_ID=%GROUP_ID_PREFIX%org.hibernate
set ARTIFACT_ID=hibernate-core
set BASE_VERSION=5.2.10.Final
set VERSION=%BASE_VERSION%-patched-play-1.5.0

set REPO_ID=sonatype-nexus-staging
set REPO_URL=https://oss.sonatype.org/service/local/staging/deploy/maven2

set SRC_DIR=../sources/%ARTIFACT_ID%/%VERSION%/patched-play-1.5.0/hibernate-core
@rem set SRC_DIR=../../../%VERSION%/patched-play-1.5.0/hibernate-core

call mvn gpg:sign-and-deploy-file -Durl=%REPO_URL% -DrepositoryId=%REPO_ID% -DpomFile=../poms/play/%ARTIFACT_ID%-%VERSION%.pom -Dfile=%PLAY_HOME%/framework/lib/%ARTIFACT_ID%-5.2.10.patched.jar -Dclassifiers=sources,javadoc -Dtypes=jar,jar -Dfiles=%SRC_DIR%/target/libs/%ARTIFACT_ID%-%BASE_VERSION%-sources.jar,%SRC_DIR%/target/libs/%ARTIFACT_ID%-%BASE_VERSION%-javadoc.jar
