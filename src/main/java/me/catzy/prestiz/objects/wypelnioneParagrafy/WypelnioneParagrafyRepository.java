// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.wypelnioneParagrafy;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "wypelnione_paragrafy", path = "wypelnione_paragrafy")
public interface WypelnioneParagrafyRepository extends JpaRepository<WypelnionyParagraf, Integer>
{
    WypelnionyParagraf getById(final int id);
}
