package me.catzy.prestiz.objects.service.conspects.fields;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service_conspect_field", path = "service_conspect_field")
public interface ConspectFieldRepository extends JpaRepository<ConspectField, Long> {
}
