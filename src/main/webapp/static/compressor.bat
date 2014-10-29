@echo off
rem author thinkgem@163.com
echo Compressor JS and CSS?
pause
cd %~dp0

call compressor\compressor.bat common
call compressor\compressor.bat jquery
call compressor\compressor.bat jquery-jbox\2.3\i18n
call compressor\compressor.bat jquery-validation\1.11.1\jquery.validate.css
call compressor\compressor.bat jquery-validation\1.11.1\jquery.validate.js
call compressor\compressor.bat jquery-validation\1.11.1\jquery.validate.method.js
call compressor\compressor.bat jquery-ztree\3.5.12\css\zTreeStyle\zTreeStyle.css
call compressor\compressor.bat treeTable\themes\default\treeTable.css
call compressor\compressor.bat treeTable\themes\vsStyle\treeTable.css
call compressor\compressor.bat treeTable\jquery.treeTable.js
call compressor\compressor.bat bootstrap\bsie\css\bootstrap-ie6.css
call compressor\compressor.bat bootstrap\bsie\js\bootstrap-ie.js
call compressor\compressor.bat modules

echo.
echo Compressor Success
pause
echo on