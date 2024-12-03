package me.catzy.prestiz.objects.service.conspects.fields;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service_conspect_field", path = "service_conspect_field")
public interface ConspectFieldRepository extends JpaRepository<ConspectField, Long> {
	
	/*@Query(value="UPDATE ConspectField f SET ")
	public void moveSortBack(ConspectField f);*/
	
	@Query(value="SELECT c FROM Conspect c WHERE c.deleted=false")
	public List<ConspectField> findAllNotDeleted();
}
