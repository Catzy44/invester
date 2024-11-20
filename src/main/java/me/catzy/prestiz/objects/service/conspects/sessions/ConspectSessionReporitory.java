package me.catzy.prestiz.objects.service.conspects.sessions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service_conspect_session", path = "service_conspect_session")
public interface ConspectSessionReporitory extends JpaRepository<ConspectSession, Long> {
}
