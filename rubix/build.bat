@echo off
pushd "%~dp0"
if not exist "bin" mkdir "bin"
javac -d "bin" *.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    if exist "bin" rmdir /s /q "bin"
    popd
    exit /b %errorlevel%
)
jar -cfe "rubix.jar" rubix.Main -C "bin" rubix
rmdir /s /q "bin"
popd
echo Build successful: rubix.jar generated.
