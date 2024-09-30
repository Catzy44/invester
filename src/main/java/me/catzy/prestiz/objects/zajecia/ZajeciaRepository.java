// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.zajecia;

import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import me.catzy.prestiz.objects.grupy.Grupa;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "zajecia", path = "zajecia")
public interface ZajeciaRepository extends JpaRepository<Zajecia, Integer>
{
    @Query("SELECT COUNT(z.id) FROM Zajecia z WHERE z.group=:gr AND MONTH(z.date)=:monthNum")
    Integer countByGroupAndMonthNum(@Param("gr") final Grupa g, @Param("monthNum") final int monthNum);
    
    @Query("SELECT COUNT(z.id) FROM Zajecia z WHERE z.group=:gr AND z.cykl=:cycleNum")
    Integer countByGroupAndCycleNum(@Param("gr") final Grupa g, @Param("cycleNum") final int cycleNum);
    
    @Query("SELECT MAX(z.cykl) FROM Zajecia z WHERE z.group=:gr")
    Integer getBiggestCycleNum(@Param("gr") final Grupa g);
    
    @Query("SELECT z FROM Zajecia z WHERE z.group=:gr AND z.date BETWEEN :dateJoined AND :dateLeft AND NOT EXISTS (SELECT id FROM AttenderSuspension zu WHERE zu.attender=:ucz AND z.date BETWEEN zu.dateFrom AND zu.dateTo) AND z.date >= :dateAttenderStartedClasses")
    Zajecia[] getClassesByAGC(@Param("dateJoined") final LocalDate dateJoined, @Param("dateLeft") final LocalDate dateLeft, @Param("gr") final Grupa gr, @Param("dateAttenderStartedClasses") final LocalDate dateAttenderStartedClasses, @Param("ucz") final Uczestnik ucz);
}
