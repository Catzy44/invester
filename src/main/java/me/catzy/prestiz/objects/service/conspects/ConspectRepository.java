package me.catzy.prestiz.objects.service.conspects;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service_conspect", path = "service_conspect")
public interface ConspectRepository extends JpaRepository<Conspect, Long> {
	
	
	@Query(value="SELECT MAX(f.sort) FROM ConspectField f WHERE f.conspect=:conspect")
	public Integer findMaxSortField(@Param("conspect") Conspect conspect);
	
	@Query(value="SELECT c FROM Conspect c WHERE c.deleted=false")
	public List<Conspect> findAllNotDeleted();
}
