// 
// Decompiled by Procyon v0.6.0
// 

package me.catzy.prestiz.objects.uczestnicy.personalData;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel = "personalInfos", path = "personalInfos")
public interface AttenderPersonalInfoRepository extends JpaRepository<AttenderPersonalInfo, Integer>
{
}
