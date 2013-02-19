@echo off
rem author thinkgem@163.com
echo Compressor JS and CSS?
pause
cd %~dp0

call compressor\compressor.bat style.css
call compressor\compressor.bat common.js
call compressor\compressor.bat wsize.js
call compressor\compressor.bat bootstrap\2.0.4\css
call compressor\compressor.bat bootstrap\2.0.4\js
call compressor\compressor.bat jquery
call compressor\compressor.bat jquery-validation
call compressor\compressor.bat mztreeview\mz*
call compressor\compressor.bat treeTable\*treeTable*
call compressor\compressor.bat jquery-jbox\2.3\i18n
call compressor\compressor.bat modules

echo.
echo Compressor Success
pause
echo on