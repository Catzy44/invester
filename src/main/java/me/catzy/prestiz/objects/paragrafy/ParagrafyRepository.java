package me.catzy.prestiz.objects.paragrafy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "paragrafy", path = "paragrafy")
public interface ParagrafyRepository extends JpaRepository<Paragraf, Integer>
{
    Paragraf getById(final int id);
    
    Paragraf getOne(final int id);
    
    @Modifying
    @Query(value = "UPDATE form_paragrafy SET ord=ord-1 WHERE ord>:selfOrd AND ord<=:moveAfterOrd AND popupId=:formularzId", nativeQuery = true)
    void moveForwards(final int selfOrd, final int moveAfterOrd, final int formularzId);
    
    @Modifying
    @Query(value = "UPDATE form_paragrafy SET ord=ord+1 WHERE ord>:moveAfterOrd AND ord<:selfOrd AND popupId=:formularzId", nativeQuery = true)
    void moveBackwards(final int selfOrd, final int moveAfterOrd, final int formularzId);
    
    List<Paragraf> getByIdIn(final int[] id);
}
