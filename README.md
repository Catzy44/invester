# **Prestiz.org API**<br/>
<br/>
*Look for legacy API in Front-End git project.*<br/>
<br/>
<br/>
every object live in:<br/>
[src/main/java/me/catzy/prestiz/objects](https://github.com/Catzy44/prestiz.org/tree/master/src/main/java/me/catzy/prestiz/objects "https://github.com/Catzy44/prestiz.org/tree/master/src/main/java/me/catzy/prestiz/objects")
every object contists of **Entity**, **Service** and **Controller**<br/>
<br/>
**Entity** definies **SQL Entity** living in MySQL database<br/>
**Service** definies all **methods** that can be executed on entity / entity set<br/>
**Controller** routes **API calls** into service<br/>
<br/>
Every entity is linked to other entities - **relations are REAL**, when u use object from another objects relation Spring'll get that object for u<br/>
<br/>
Security is straight-away.<br/>
**Every logged user has access to EVERYTHING**<br/>
Entitled thru [security/PrzemoFilter.java](https://github.com/Catzy44/prestiz.org/blob/master/src/main/java/me/catzy/prestiz/security/PrzemoFilter.java "security/PrzemoFilter.java")
<br/>
project consists of **two gradle conf files**:<br/>
<br/>
**application.properties**<br/>
for production<br/>
<br/>
**application.remote.properties**<br/>
for local testing<br/>
<br/>
u can build project using<br/>
**gradle assemble**<br/>
look for generated jar file in **/build/libs/**<br/>
replace the file on server<br/>
**on server there is man.sh file,**<br/>
u can manage ur spring instance throught it.<br/>
<br/>
./man stop,start,con<br/>
(con shows u spring terminal)<br/>
<br/>
u can start **local server** using<br/>
**bootnow.bat** file<br/>
<br/>
## **API technologies:**<br/>
<br/>
Java Development Kit 17<br/>
Spring Boot<br/>
Hibernate<br/>
Project Lombok<br/>
GOOGLE GSON <br/>
MySQL<br/>
JPA/JPQL<br/>
<br/>
ECLIPSE IDE<br/>
<br/>
<br/>
###### //catzy44<br/>
