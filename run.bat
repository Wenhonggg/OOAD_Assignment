@echo off
REM Create bin directory if it doesn't exist
if not exist "bin" mkdir "bin"

REM Compile all Java files and place class files in bin
javac -d bin -cp ".;lib\*" src\*.java

REM Run the program from the bin directory
java -cp "bin;lib\*" LoginApp

pause