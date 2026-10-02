@echo off
pushd "%~dp0"
if exist "bin" rmdir /s /q "bin"
mkdir "bin"

javac -d "bin" *.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    popd
    exit /b %errorlevel%
)

jar -cfe "rubix.jar" rubix.Main -C "bin" .
rmdir /s /q "bin"
popd
echo Build successful: rubix.jar generated.