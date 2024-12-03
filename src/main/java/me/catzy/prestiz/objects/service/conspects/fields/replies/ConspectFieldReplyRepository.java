package me.catzy.prestiz.objects.service.conspects.fields.replies;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import me.catzy.prestiz.objects.instruktorzy.Instruktor;
import me.catzy.prestiz.objects.service.conspects.fields.ConspectField;

@RepositoryRestResource(collectionResourceRel = "service_conspect_field_reply", path = "service_conspect_field_reply")
public interface ConspectFieldReplyRepository extends JpaRepository<ConspectFieldReply, Long> {
	@Query(value="SELECT r FROM ConspectFieldReply r WHERE r.conspectField=:field AND r.instructor=:instructor")
	List<ConspectFieldReply> getInstructorReplies(@Param("field") ConspectField field,@Param("instructor") Instruktor instructor);
}
