```Look for legacy API in Front-End git project.

every object live in:
src/main/java/me/catzy/prestiz/objects
every object contists of Entity, Service and Controller

Entity definies SQL Entity living in MySQL database
Service definies all methods that can be executed on entity / entity set
Controller routes API calls into service

Every entity is linked to other entities - relations are REAL, when u use object from another objects relation Spring Boot'll get that object for u

Security is straight-away.
Every logged user has access to EVERYTHING
Entitled thru PrzemoFilter.class 

project consists of two conf files:

application.properties
for production
and
application.remote.properties
for local testing

u can build project using
gradle assemble
look for generated jar file in /build/libs/
replace the file on server
on server there is man.sh file,
u can manage ur spring instance throught it.

./man <stop,start,con>
(con shows u spring terminal)

u can start local server using
bootnow.bat file

API-side technologies:

Java Development Kit 17
Spring Boot
Hibernate
Project Lombok
GOOGLE GSON 
MySQL
JPA/JPQL

ECLIPSE IDE```


//catzy44
