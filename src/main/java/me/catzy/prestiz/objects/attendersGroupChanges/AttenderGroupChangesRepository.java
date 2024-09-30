// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.attendersGroupChanges;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "attenderGroupChanges", path = "attenderGroupChanges")
public interface AttenderGroupChangesRepository extends JpaRepository<AttenderGroupChange, Integer>
{
    AttenderGroupChange getById(final int id);
    
    AttenderGroupChange[] getByIdIn(final int[] id);
}
