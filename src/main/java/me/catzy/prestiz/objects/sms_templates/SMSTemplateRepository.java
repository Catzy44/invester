// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.sms_templates;

import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "sms_templates", path = "sms_templates")
public interface SMSTemplateRepository extends JpaRepository<SMSTemplate, Integer>
{
    SMSTemplate getById(final int id);
    
    @Query("SELECT t FROM SMSTemplate t WHERE t.type=:type")
    List<SMSTemplate> getByType(@Param("type") final int type);
}
