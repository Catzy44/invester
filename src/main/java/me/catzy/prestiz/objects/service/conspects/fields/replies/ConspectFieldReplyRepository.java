package me.catzy.prestiz.objects.service.conspects.fields.replies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "service_conspect_field_reply", path = "service_conspect_field_reply")
public interface ConspectFieldReplyRepository extends JpaRepository<ConspectFieldReply, Long> {
}
