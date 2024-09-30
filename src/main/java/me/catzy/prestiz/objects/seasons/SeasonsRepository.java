// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.seasons;

import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "seasons", path = "seasons")
public interface SeasonsRepository extends JpaRepository<Season, Integer>
{
    List<Season> getByIdIn(final int[] id);
    
    Season getById(final int id);
    
    Season getOne(final int id);
    
    @Query("SELECT s FROM Season s")
    List<Season> getAll();
}
