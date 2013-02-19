@echo off
echo [INFO] Re-create the schema and provision the sample data.
pause

cd %~dp0
cd ..
cd ..

set path=%MAVEN_HOME%/bin;%windir%/system32;%path%
call mvn antrun:run -Prefresh-db

cd bin
cd refresh-db
pause