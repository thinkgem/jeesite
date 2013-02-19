rem @echo off
rem author thinkgem@163.com
rem echo Compressor JS and CSS?
rem pause
setlocal enabledelayedexpansion
rem cd %~dp0
rem cd ..
echo.
echo Compressor path: %1
echo.
(dir %1 /aa /b /s | findstr /e /c:"js") > compressor.tmp
for /f %%i in (compressor.tmp) do (
	set str=%%i
	set first=!str:~0,-3!
	set last=!str:~-6!
	if !last! neq min.js (
		echo in %%i
		java -jar compressor\yuicompressor-2.4.7.jar --line-break 2048 --type js --charset utf-8 -o !first!.min.js %%i
		echo to !first!.min.js
	)
)
(dir %1 /aa /b /s | findstr /e /c:"css") > compressor.tmp
for /f %%i in (compressor.tmp) do (
	set str=%%i
	set first=!str:~0,-4!
	set last=!str:~-7!
	if !last! neq min.css (
		echo in %%i
		java -jar compressor\yuicompressor-2.4.7.jar --line-break 2048 --type css --charset utf-8 -o !first!.min.css %%i
		echo to !first!.min.css
	)
)
rem echo Compressor Success
rem echo Delete temp file.
del compressor.tmp
rem pause
rem echo on