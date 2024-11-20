package me.catzy.prestiz.objects.service.conspects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service_conspect", path = "service_conspect")
public interface ConspectsRepository extends JpaRepository<Conspect, Long> {
}
