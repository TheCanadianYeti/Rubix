@echo off
if not exist "%~dp0rubix.jar" (
    call "%~dp0build.bat"
)
java -jar "%~dp0rubix.jar" %*
