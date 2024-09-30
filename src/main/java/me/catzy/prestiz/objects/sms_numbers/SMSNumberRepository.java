// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.sms_numbers;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "sms_numbers", path = "sms_numbers")
public interface SMSNumberRepository extends JpaRepository<SMSNumber, Integer>
{
    SMSNumber findByNumber(final String number);
}
