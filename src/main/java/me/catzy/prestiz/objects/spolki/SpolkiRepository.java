// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.spolki;

import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "spolki", path = "spolki")
public interface SpolkiRepository extends JpaRepository<Spolka, Integer>
{
    List<Spolka> getByIdIn(final int[] id);
    
    Spolka getById(final int id);
}
