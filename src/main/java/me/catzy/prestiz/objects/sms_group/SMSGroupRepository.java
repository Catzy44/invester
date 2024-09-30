// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.sms_group;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "sms_groups", path = "sms_groups")
public interface SMSGroupRepository extends JpaRepository<SMSGroup, Integer>
{
    SMSGroup getById(final int id);
    
    @Query(value = "SELECT * FROM sms_codes WHERE code=:code AND active=1", nativeQuery = true)
    SMSGroup findByCode(@Param("code") final int code);
}
