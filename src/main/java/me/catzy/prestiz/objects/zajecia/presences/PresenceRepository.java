// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.zajecia.presences;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import me.catzy.prestiz.objects.zajecia.Zajecia;

@RepositoryRestResource(collectionResourceRel = "presences", path = "presences")
public interface PresenceRepository extends JpaRepository<Presence, Integer>
{
    @Query("SELECT p FROM Presence p WHERE p.classes=:zz AND p.attender=:uu")
    Presence findByClassAndAttender(@Param("uu") final Uczestnik u, @Param("zz") final Zajecia z);
    
    Presence getById(final int id);
}
