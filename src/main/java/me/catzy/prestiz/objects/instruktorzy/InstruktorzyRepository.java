// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.instruktorzy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "instruktorzy", path = "instruktorzy")
public interface InstruktorzyRepository extends JpaRepository<Instruktor, Long>
{
    Instruktor getById(final int id);
    
    Instruktor getByNick(final String nick);
}
