@REM @echo off
@REM REM Create bin directory if it doesn't exist
@REM if not exist "bin" mkdir "bin"

@REM REM Compile all Java files and place class files in bin
@REM javac -d bin -cp ".;lib\*" src\*.java

@REM REM Run the program from the bin directory
@REM java -cp "bin;lib\*" LoginApp

@REM pause

@echo off
if not exist "bin" mkdir "bin"
javac --enable-preview -d bin -cp ".;lib\*" src\main\component\*.java src\main\*.java
java --enable-preview -cp "bin;lib\*" main.LoginPage
pause