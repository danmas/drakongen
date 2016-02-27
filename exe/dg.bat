REM Generation  src classes from  scheme.

REM chcp 1251
chcp 65001

set DRAKON_GEN="C:\ERV\PROJECTS\DrakonGen\exe\drakongen.jar"
set BASE_DIR=%1

java -jar %DRAKON_GEN% %BASE_DIR% %2 
