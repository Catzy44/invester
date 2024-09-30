// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.miasta;

import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "miasta", path = "miasta")
public interface MiastaRepository extends JpaRepository<Miasto, Integer>
{
    List<Miasto> getByIdIn(final int[] id);
    
    List<Miasto> findAll();
}
