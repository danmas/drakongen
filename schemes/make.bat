chcp 1251
rem chcp 65001

rem HOME
rem set JAVA_PATH=C:\Program Files\Java\jdk1.8.0_91\bin\
rem RDTEX
rem set JAVA_PATH=C:\Program Files\Java\jdk1.8.0_111\bin\
rem NOTE-HP
rem set JAVA_PATH=C:\ERV\Java\jre1.8.0_91\bin\ 
set JAVA_PATH=C:\ERV\Java\jdk1.8.0_91\bin\

cd ..\develop\WRK_DragonGen2J\DragonGen2J
"%JAVA_PATH%\javac.exe" -encoding UTF8 -sourcepath .\src -d .\bin1 -cp .\libs\blueprints-core-1.2.jar;.\libs\antlr-3.4-complete.jar;.\libs\commons-lang3-3.1.jar;.\libs\json-simple-1.1.jar;.\libs\log4j-1.2.8.jar;.\libs\jsch-0.1.53.jar;.\libs\postgresql-9.4.1212.jre6.jar;.\libs\ojdbc7.jar .\src\ru\erv\drakongen\**.java
"%JAVA_PATH%jar.exe" cmf .\MANIFEST.MF ..\..\..\exe\drakongen_NEW.jar -C .\bin1\ .
"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar .\org
"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\libs blueprints-core-1.2.jar

"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\libs json-simple-1.1.jar
"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\libs log4j-1.2.8.jar
"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\libs jsch-0.1.53.jar
"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\libs ojdbc7.jar
"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\ log4j.properties
"%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\libs postgresql-9.4.1212.jre6.jar 