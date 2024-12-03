package me.catzy.prestiz.objects.service.conspect.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service_conspect_category", path = "service_conspect_category")
public interface ConspectCategoryRepository extends JpaRepository<ConspectCategory, Long> {
}
