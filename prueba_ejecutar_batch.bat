@echo off
:loop
mvn spring-boot:run
if %errorlevel% neq 0 (
    echo Tarea cancelada sin confirmar.
    goto :end
)
goto :loop
:end
