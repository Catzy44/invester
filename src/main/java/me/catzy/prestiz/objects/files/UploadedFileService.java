package me.catzy.prestiz.objects.files;

import org.springframework.stereotype.Service;

import me.catzy.prestiz.generic.GenericServiceImpl;

@Service
public class UploadedFileService extends GenericServiceImpl<UploadedFile, Long> {
	public UploadedFileService(UploadedFileRepository repository) {
		super(repository);
	}
}
