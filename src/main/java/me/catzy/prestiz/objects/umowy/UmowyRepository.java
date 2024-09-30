// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.umowy;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "umowy", path = "umowy")
public interface UmowyRepository extends JpaRepository<Umowa, Integer>
{
    Umowa getById(final int id);
}
