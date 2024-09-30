// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.uczestnicy_dane;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "uczestnicy_dane", path = "uczestnicy_dane")
public interface UczestnicyDaneRepository extends JpaRepository<UczestnikDane, Integer>
{
    UczestnikDane findById(final int id);
    
    UczestnikDane getOne(final int id);
}
