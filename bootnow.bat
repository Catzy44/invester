@echo off
title PRESTIZ BACKEND
call gradlew.bat bootRun --console plain --args='--spring.config.location=classpath:/application.remote.properties'
pause