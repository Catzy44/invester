package me.catzy.prestiz.objects.miejsca;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.prestiz.objects.seasons.Season;

@RepositoryRestResource(collectionResourceRel = "miejsca", path = "miejsca")
public interface MiejscaRepository extends JpaRepository<Miejsce, Integer>
{
    @Query(value = "SELECT * FROM Miejsca WHERE name LIKE %:name% LIMIT :limit", nativeQuery = true)
    List<Miejsce> searchByName(@Param("name") final String name, @Param("limit") final int limit);
    
    List<Miejsce> getByIdIn(final int[] id);
    
    Miejsce getById(final int id);
    
    Miejsce getOne(final int id);
    
    @Query("SELECT m FROM Miejsce m JOIN Spolka s ON m.company=s WHERE m.name LIKE %:name% AND s.season=:s")
    List<Miejsce> findMiejsceByName(@Param("name") final String name, @Param("s") final Season s, final Pageable pageable);
    
    @Query("SELECT s FROM Miejsce s")
    List<Miejsce> getAll();
}
