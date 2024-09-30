// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.miasta.closedMonths;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "closedMonths", path = "closedMonths")
public interface ClosedMonthRepository extends JpaRepository<ClosedMonth, Integer>
{
    List<ClosedMonth> getByIdIn(final int[] id);
    
    List<ClosedMonth> findAll();
    
    @Query("SELECT m FROM ClosedMonth m WHERE m.attender=:u AND m.month=:m")
    ClosedMonth findByAttenderAndMonth(@Param("u") final Uczestnik u, @Param("m") final int m);
}
