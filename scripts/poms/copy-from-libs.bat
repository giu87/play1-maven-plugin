setlocal 

call ..\set-play-home-1.1.bat
call copy-framework.bat %PLAY_HOME% play\1.1

call ..\set-play-home-1.1.1.bat
call copy-framework.bat %PLAY_HOME% play\1.1.1

call ..\set-play-home-1.1.2.bat
call copy-framework.bat %PLAY_HOME% play\1.1.2

call ..\set-play-home-1.2.bat
call copy-framework.bat %PLAY_HOME% play\1.2

call ..\set-play-home-1.2.1.bat
call copy-framework.bat %PLAY_HOME% play\1.2.1

call ..\set-play-home-1.2.1.1.bat
call copy-framework.bat %PLAY_HOME% play\1.2.1.1

call ..\set-play-home-1.2.2.bat
call copy-framework.bat %PLAY_HOME% play\1.2.2

call ..\set-play-home-1.2.3.bat
call copy-framework.bat %PLAY_HOME% play\1.2.3

endlocal
