chcp 1251
rem chcp 65001

rem HOME
rem set JAVA_PATH=C:\Program Files\Java\jdk1.8.0_91\bin\
rem RDTEX
set JAVA_PATH=C:\Program Files\Java\jdk1.8.0_111\bin\


rem "%JAVA_PATH%\javac.exe" -encoding UTF8 -sourcepath .\src -d .\bin1 -cp .\libs\blueprints-core-1.2.jar;.\libs\antlr-3.4-complete.jar;.\libs\commons-lang3-3.1.jar .\src\ru\erv\drakongen\**.java
rem "%JAVA_PATH%jar.exe" cmf .\MANIFEST.MF ..\..\..\exe\drakongen_NEW.jar -C .\bin1\ .
rem "%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar .\org
rem "%JAVA_PATH%jar.exe" uf ..\..\..\exe\drakongen_NEW.jar -C .\libs blueprints-core-1.2.jar

"%JAVA_PATH%\javac.exe" -encoding UTF8 -sourcepath .\src -d .\bin1  .\src\main\java\org\json\simple\parser\**.java
"%JAVA_PATH%\javac.exe" -encoding UTF8 -sourcepath .\src -d .\bin1  .\src\main\java\org\json\simple\**.java


