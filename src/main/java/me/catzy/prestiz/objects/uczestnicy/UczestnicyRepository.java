// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.uczestnicy;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "uczestnicy", path = "uczestnicy")
public interface UczestnicyRepository extends JpaRepository<Uczestnik, Integer>
{
    @Query(nativeQuery = true, value = "SELECT * FROM Uczestnicy WHERE approved=0 AND idMiejsca=:miejsceId")
    List<Uczestnik> getChetni(@Param("miejsceId") final int miejsceId);
    
    List<Uczestnik> getByIdIn(final int[] ids);
    
    @Query(nativeQuery = true, value = "SELECT * FROM Uczestnicy WHERE RIGHT(number,9) IN :nums")
    List<Uczestnik> getByNumbersIn(@Param("nums") final String[] nums);
    
    Uczestnik findById(final int id);
    
    Uczestnik getOne(final int id);
    
    @Query(nativeQuery = true, value = "SELECT * FROM Uczestnicy WHERE telefonOpiekuna=:number LIMIT 1")
    Uczestnik byPhoneNumberExact(@Param("number") final String number);
    
    @Query("SELECT new me.catzy.prestiz.objects.uczestnicy.Uczestnik(u.id,u.imie,u.nazwisko,u.place,u.season) FROM Uczestnik u WHERE u.telefonOpiekuna=:number")
    List<Uczestnik> getUczestnicyByPn(@Param("number") final String number);
    
    @Query("SELECT u FROM Uczestnik u WHERE u.telefonOpiekuna IS NOT NULL AND u.telefonOpiekuna != ''")
    List<Uczestnik> getAllWithValidPhoneNumbers();
    
    @Query("SELECT u FROM Uczestnik u WHERE CONCAT(u.imie,' ',u.nazwisko,' ',u.telefonOpiekuna) LIKE %:name% AND u.place IS NOT NULL AND u.group IS NOT NULL AND u.group.id!=-1 AND u.group.id!=0")
    List<Uczestnik> findUczestnikByString(@Param("name") final String name, final Pageable pageable);
}
