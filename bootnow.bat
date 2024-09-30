@echo off
call gradlew.bat bootRun --console plain --args='--spring.config.location=classpath:/application.remote.properties'
pause