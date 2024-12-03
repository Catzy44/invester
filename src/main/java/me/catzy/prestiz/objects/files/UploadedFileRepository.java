package me.catzy.prestiz.objects.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "uploaded_file", path = "uploaded_file")
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
}
