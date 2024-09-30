// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.grupy;

import org.springframework.data.jpa.repository.Query;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;
import java.util.List;
import org.springframework.data.repository.query.Param;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "grupy", path = "grupy")
public interface GrupyRepository extends JpaRepository<Grupa, Integer>
{
    Grupa getById(final int id);
    
    Grupa[] getByIdIn(final int[] id);
    
    @Query(value = "SELECT u FROM Uczestnik u WHERE u.place=:m AND (u.groupId=0 OR u.groupId=-1)", nativeQuery = false)
    List<Uczestnik> getDischargedAttenders(@Param("m") final Miejsce m);
}
