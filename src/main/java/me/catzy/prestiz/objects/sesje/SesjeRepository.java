// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.sesje;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "sesje", path = "sesje")
public interface SesjeRepository extends JpaRepository<Sesja, Integer>
{
    @Query(value = "SELECT * FROM Sesje WHERE session_id=:sid", nativeQuery = true)
    Sesja getActiveSessionByKey(@Param("sid") final String sid);
    
    Sesja getById(final int id);
}
