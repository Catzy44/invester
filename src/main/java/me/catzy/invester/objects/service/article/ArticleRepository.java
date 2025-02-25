package me.catzy.invester.objects.service.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "article", path = "article")
public interface ArticleRepository extends JpaRepository<Article, Long> {
	
	
	/*@Query(value="SELECT MAX(f.sort) FROM ConspectField f WHERE f.conspect=:conspectField")
	public Integer findMaxSortField(@Param("conspectField") Article conspect);
	
	@Query(value="SELECT c FROM Article c WHERE c.deleted=false")
	public List<Article> findAllNotDeleted();*/
	
	public Article findByUrl(String url);
}
