// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.platnosci;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import java.util.List;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "payments", path = "payments")
public interface PaymentsRepository extends JpaRepository<Payment, Integer>
{
    List<Payment> getByIdIn(final int[] ids);
    
    Payment findById(final int id);
    
    Payment getOne(final int id);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.attender=:u")
    Float getTotalMoneyPaidByAttender(@Param("u") final Uczestnik u);
}
