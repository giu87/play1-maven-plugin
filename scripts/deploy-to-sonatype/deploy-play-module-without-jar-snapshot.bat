@rem Call it once:
@rem call mvn clean install --file %PLAY_HOME%/modules/parent/pom.xml

set GROUP_ID_PREFIX=com.google.code.maven-play-plugin.
set GROUP_ID=%GROUP_ID_PREFIX%org.playframework.modules.%MODULE_NAME%
set ARTIFACT_ID=play-%MODULE_NAME%
set SRC_DIR=%PLAY_HOME%/modules/%MODULE_NAME%

set REPO_ID=sonatype-nexus-snapshots
set REPO_URL=https://oss.sonatype.org/content/repositories/snapshots

call mvn clean package -Pdist --file %SRC_DIR%/pom-build-dist.xml

call mvn gpg:sign-and-deploy-file -Durl=%REPO_URL% -DrepositoryId=%REPO_ID% -DpomFile=../poms/play/%VERSION%/modules/%MODULE_NAME%/pom-dist.xml -Dfile=%SRC_DIR%/target/%ARTIFACT_ID%-%VERSION%-module.zip -Dclassifier=module -Dpackaging=zip -Dclassifiers=module-min -Dtypes=zip -Dfiles=%SRC_DIR%/target/%ARTIFACT_ID%-%VERSION%-module-min.zip