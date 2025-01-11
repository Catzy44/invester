// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.instruktorzy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.prestiz.objects.instruktorzy.InstruktorzyController.ConspectCountersDTO;
import me.catzy.prestiz.objects.instruktorzy.InstruktorzyController.ConspectTimesDTO;

@RepositoryRestResource(collectionResourceRel = "instruktorzy", path = "instruktorzy")
public interface InstruktorzyRepository extends JpaRepository<Instruktor, Long>
{
    Instruktor getById(final int id);
    
    Instruktor getByNick(final String nick);

    @Query(value = "SELECT "
    		+ "new me.catzy.prestiz.objects.instruktorzy.InstruktorzyController$ConspectTimesDTO("
    		+ "cs.conspect,"
    		+ "SUM(TIMESTAMPDIFF(SECOND,cs.startTimestamp,cs.endTimestamp)),"
    		+ "MIN(cs.startTimestamp),MAX(cs.startTimestamp)"
    		
    		//FINDS ConspectFields without Replies
//			+ "("
//			+ "SELECT COUNT(cf) "//count JOINED Replies of ConspectFields
//			
//			+ "FROM ConspectField cf  JOIN ConspectFieldReply cfr ON cfr.conspectField=cf "//fields joined with replies
//			
//			+ "WHERE cf.conspect=cs.conspect "//conspectField fields of this conspectField
//			+ "AND cfr.instructor=:i "//replies of this instructor
//			+ "GROUP BY cf "//group by ConspectFields
//			+ "HAVING COUNT(cfr) = 0 "
//			+ ")"


    		//FINDS ConspectFields without Replies
			//+ "(NULL)"
    		
    		+ ") FROM ConspectSession cs "
    		+ "WHERE cs.instructor=:i "
    		+ "GROUP BY cs.conspect")
    public List<ConspectTimesDTO> getConspectTimes(@Param("i") Instruktor i);
    
    /*@Query(value = "SELECT new me.catzy.prestiz.objects.instruktorzy.InstruktorzyController$ConspectCountersDTO("
    		+ "cf,"
    		+ "COUNT(cfr)"
    		+ ")"
    		+ "FROM ConspectField cf "
    		+ "LEFT JOIN ConspectFieldReply cfr ON cfr.conspectField=cf AND cfr.instructor=:i"
    		+ "WHERE cf.type=4 "
//    		+ "WHERE cf.type=4 AND (cfr.instructor=:i OR cfr.instructor IS NULL) "
    		+ "GROUP BY cf")*/
    @Query(value = "SELECT new me.catzy.prestiz.objects.instruktorzy.InstruktorzyController$ConspectCountersDTO("
    		+ "cf,"
    		+ "COUNT(cfr)"
    		+ ")"
    		+ "FROM ConspectField cf "
    		+ "LEFT JOIN ConspectFieldReply cfr ON cfr.conspectField=cf AND cfr.instructor=:i "
    		+ "WHERE cf.type=4 AND (cfr IS NULL OR cfr.instructor = :i) "
    		+ "GROUP BY cf")
    public List<ConspectCountersDTO> getConspectFieldCounters(@Param("i") Instruktor i);
}
