package me.catzy.prestiz.objects.service.conspects;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@RepositoryRestResource(collectionResourceRel = "service_conspect", path = "service_conspect")
public interface ConspectRepository extends JpaRepository<Conspect, Long> {
	
	
	@Query(value="SELECT MAX(f.sort) FROM ConspectField f WHERE f.conspect=:conspectField")
	public Integer findMaxSortField(@Param("conspectField") Conspect conspect);
	
	@Query(value="SELECT c FROM Conspect c WHERE c.deleted=false")
	public List<Conspect> findAllNotDeleted();
	
	@Query(value="SELECT cf FROM ConspectField cf WHERE cf.type=4 AND cf.conspect=:c")
	public List<ConspectField> getQuestionFields(@Param("c") Conspect c);
}
