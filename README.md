<h1 id="h1--strong-prestiz-org-api-strong-"><a name="<strong>Prestiz.org API</strong>" class="reference-link"></a><span class="header-link octicon octicon-link"></span><strong>Prestiz.org BACKEND</strong></h1>
<p><em>Look for legacy API in Front-End git project:</em></p>
<a href="https://github.com/Catzy44/prestiz.org-FRONTEND">link to frontend</a>
<p>every object live in:<br><a href="https://github.com/Catzy44/prestiz.org/tree/master/src/main/java/me/catzy/prestiz/objects" title="https://github.com/Catzy44/prestiz.org/tree/master/src/main/java/me/catzy/prestiz/objects">src/main/java/me/catzy/prestiz/objects</a><br>every object contists of <strong>Entity</strong>, <strong>Service</strong> and <strong>Controller</strong></p>
<p><strong>Entity</strong> definies <strong>SQL Entity</strong> living in MySQL database<br><strong>Service</strong> definies all <strong>methods</strong> that can be executed on entity / entity set<br><strong>Controller</strong> routes <strong>API calls</strong> into service</p>
<p>Every entity is linked to other entities - <strong>relations are REAL</strong>, when u use object from another objects relation Spring’ll get that object for u</p>
<p>Security is straight-away.<br><strong>Every logged user has access to EVERYTHING</strong><br>Entitled thru <a href="https://github.com/Catzy44/prestiz.org/blob/master/src/main/java/me/catzy/prestiz/security/PrzemoFilter.java" title="security/PrzemoFilter.java">security/PrzemoFilter.java</a></p>
<p>project consists of <strong>two gradle conf files</strong>:</p>
<p><strong>application.properties</strong><br>for production</p>
<p><strong>application.remote.properties</strong><br>for local testing</p>
<p>u can build project using<br><strong>gradle assemble</strong><br>look for generated jar file in <strong>/build/libs/</strong><br>replace the file on server<br><strong>on server there is man.sh file,</strong><br>u can manage ur spring instance throught it.</p>
<p>./man stop,start,con<br>(con shows u spring terminal)</p>
<p>u can start <strong>local server</strong> using<br><strong>bootnow.bat</strong> file</p>
<h2 id="h2--strong-api-technologies-strong-"><a name="<strong>API technologies:</strong>" class="reference-link"></a><span class="header-link octicon octicon-link"></span><strong>API technologies:</strong></h2>
<p>Java Development Kit 17<br>Spring Boot<br>Hibernate<br>Project Lombok<br>GOOGLE GSON<br>MySQL<br>JPA/JPQL</p>
<p>ECLIPSE IDE</p>
<h6 id="h6--catzy44"><a name="//catzy44" class="reference-link"></a><span class="header-link octicon octicon-link"></span>//catzy44</h6>
