// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.sms;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@RepositoryRestResource(collectionResourceRel = "sms", path = "sms")
public interface SMSRepository extends JpaRepository<SMS, Integer>
{
    @Modifying
    @Query(value = "UPDATE sms SET status=:status WHERE id IN :ids", nativeQuery = true)
    void updateStatusByIds(@Param("ids") final int[] ids, @Param("status") final int status);
    
    @Query(value = "SELECT * FROM sms WHERE status=0 AND type=0", nativeQuery = true)
    List<SMS> getAllToSend();
    
    @Query(value = "SELECT * FROM sms WHERE uczestnikId=:id ORDER BY created_timestamp DESC LIMIT 1", nativeQuery = true)
    SMS getLatestByUserId(@Param("id") final int id);
    
    @Query(value = "SELECT COUNT(id) FROM sms WHERE type=1 AND read_status=0", nativeQuery = true)
    int getUnreadSMSCount();
    
    @Query(value = "SELECT DISTINCT number FROM sms", nativeQuery = true)
    List<String> getAllUniquePhoneNumbers();
    
    SMS getById(final int id);
    
    @Query("SELECT new me.catzy.prestiz.objects.sms.SMS(m.id,m.status,m.readStatus,m.content,m.number,m.createdTimestamp,m.sentTimestamp,"
    		+ "(SELECT CAST(COUNT(s.id) AS int) FROM SMS s WHERE s.number=m.number AND s.type=1 AND s.readStatus=0))"
    		
    		+ " FROM SMS m "
    			+ "JOIN ("
    			+ "SELECT s.number AS number, MAX(s.createdTimestamp) AS max_createdTimestamp "
    			+ "FROM SMS s WHERE s.number IS NOT NULL GROUP BY s.number)"
    		+ " AS latest_msg ON m.number = latest_msg.number AND m.createdTimestamp = latest_msg.max_createdTimestamp "
    		
    		+ "JOIN (SELECT ROW_NUMBER() OVER (PARTITION BY a.number ORDER BY a.createdTimestamp DESC) AS intRow,a.id AS id FROM SMS a ) "
    		+ "AS idk ON m.id=idk.id WHERE idk.intRow=1 ORDER BY m.readStatus ASC, m.createdTimestamp DESC LIMIT 100")
    List<SMS> getSMSNews();
    
    @Query("SELECT new me.catzy.prestiz.objects.sms.SMS(m.id,m.status,m.readStatus,m.content,m.number,m.createdTimestamp,m.sentTimestamp,(SELECT CAST(COUNT(s.id) AS int) FROM SMS s WHERE s.number=m.number AND s.type=1 AND s.readStatus=0)) FROM SMS m JOIN (   SELECT s.number AS number,  MAX(s.createdTimestamp) AS max_createdTimestamp FROM SMS s  WHERE s.number IS NOT NULL   GROUP BY s.number) AS latest_msg ON m.number = latest_msg.number AND m.createdTimestamp = latest_msg.max_createdTimestamp JOIN (SELECT ROW_NUMBER() OVER (PARTITION BY a.number ORDER BY a.createdTimestamp DESC) AS intRow,a.id AS id FROM SMS a ) AS idk ON m.id=idk.id WHERE idk.intRow=1 AND m.number LIKE %:number%ORDER BY m.createdTimestamp DESC")
    List<SMS> searchInConversationsByNumber(@Param("number") final String number, final Pageable pageable);
    
    @Modifying
    @Query("UPDATE SMS s SET s.readStatus=1 WHERE s.uczestnik=:pu")
    void markAllUczestnikMessagesAsRead(@Param("pu") final Uczestnik pu);
    
    @Modifying
    @Query("UPDATE SMS s SET s.readStatus=1 WHERE s.number=:num")
    void markAllNumberMessagesAsRead(@Param("num") final String num);
    
    @Query(value = "SELECT * FROM sms WHERE number LIKE %:number", nativeQuery = true)
    List<SMS> getAllMessagesByNumber(@Param("number") final String number);
    
    @Query("SELECT COUNT(s.id) FROM SMS s WHERE s.number=:number AND s.type=1 AND s.readStatus=0")
    int getUnreadCountByNumber(@Param("number") final String number);
    
    @Query("SELECT new me.catzy.prestiz.objects.sms.SMSService$FoundConversation(u,p,ai,pi,(SELECT COUNT(s.id) FROM SMS s WHERE s.number=u.telefonOpiekuna AND s.type=1 AND s.readStatus=0)) FROM Uczestnik u JOIN Miejsce p ON u.place=p JOIN AttenderPersonalInfo pi ON u.parentInfo = pi JOIN AttenderPersonalInfo ai ON u.attenderInfo = ai WHERE CONCAT_WS(' ',pi.firstName,pi.lastName,pi.number) LIKE %:name% OR CONCAT_WS(' ',ai.firstName,ai.lastName,ai.number) LIKE %:name% AND p IS NOT NULL AND u.group IS NOT NULL AND u.group.id!=-1 AND u.group.id!=0")
    List<SMSService.FoundConversation> findAttendersByStrInclUnreadSMSCount(@Param("name") final String name, final Pageable pageable);
}
