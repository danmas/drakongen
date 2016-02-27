REM SET FROM_CODE=utf-8
SET FROM_CODE=cp1251
SET TO_CODE=utf-8


c:\iconv\bin\iconv.exe -f %FROM_CODE% -t %TO_CODE% DrakonGen2.java > DrakonGen2_2.java
move DrakonGen2_2.java DrakonGen2.java
c:\iconv\bin\iconv.exe -f %FROM_CODE% -t %TO_CODE% DrakonUtils.java > DrakonUtils_2.java
move DrakonUtils_2.java DrakonUtils.java

REM chcp 1251

PAUSE
