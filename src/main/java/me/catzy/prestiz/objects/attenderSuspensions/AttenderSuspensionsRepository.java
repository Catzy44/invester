// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.attenderSuspensions;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "attenderSuspensions", path = "attenderSuspensions")
public interface AttenderSuspensionsRepository extends JpaRepository<AttenderSuspension, Integer>
{
    AttenderSuspension getById(final int id);
    
    AttenderSuspension[] getByIdIn(final int[] id);
}
