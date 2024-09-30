// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.formularze;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "formularze", path = "formularze")
public interface FormularzeRepository extends JpaRepository<Formularz, Integer>
{
    @Query(value = "SELECT * FROM form_popupy WHERE miejsceId=:idMiejsca AND active=1 LIMIT 1", nativeQuery = true)
    Formularz getAktywnyFormularz(final int idMiejsca);
    
    @Query(value = "SELECT * FROM form_popupy WHERE active=1", nativeQuery = true)
    List<Formularz> getAllActive();
    
    Formularz getById(final int id);
    
    Formularz getOne(final int id);
}
