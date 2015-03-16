@echo off
rem author thinkgem@163.com
echo Compressor JS and CSS?
pause
cd %~dp0

call compressor\compressor.bat bootstrap\bsie\css\bootstrap-ie6.css
call compressor\compressor.bat bootstrap\bsie\js\bootstrap-ie.js
call compressor\compressor.bat common
call compressor\compressor.bat jquery-plugin
call compressor\compressor.bat jquery-select2\3.4\select2.css
call compressor\compressor.bat jquery-select2\3.4\select2.js
call compressor\compressor.bat jquery-jbox\2.3\Skins\Bootstrap\jbox.css
call compressor\compressor.bat jquery-jbox\2.3\jquery.jBox-2.3.js
call compressor\compressor.bat jquery-validation\1.11.0\jquery.validate.css
call compressor\compressor.bat jquery-validation\1.11.0\jquery.validate.js
call compressor\compressor.bat jquery-ztree\3.5.12\css\zTreeStyle\zTreeStyle.css
rem call compressor\compressor.bat jerichotab\css\jquery.jerichotab.css
rem call compressor\compressor.bat jerichotab\js\jquery.jerichotab.js
call compressor\compressor.bat treeTable\themes\vsStyle\treeTable.css
call compressor\compressor.bat treeTable\jquery.treeTable.js
call compressor\compressor.bat supcan\supcan.js
call compressor\compressor.bat modules

echo.
echo Compressor Success
pause
echo on