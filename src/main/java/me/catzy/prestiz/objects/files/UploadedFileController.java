package me.catzy.prestiz.objects.files;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import me.catzy.prestiz.RandomString;
import me.catzy.prestiz.ToolBox;
import me.catzy.prestiz.generic.GenericController;

@RestController
@RequestMapping({ "/uploaded_file"})
public class UploadedFileController extends GenericController<UploadedFile, Long> {
	@Autowired UploadedFileService service;
	
	public UploadedFileController(UploadedFileService service) {
        super(service);
    }
	
	String uploadDir = "C:/Users/catzy/Desktop/uploads/";
	
	@PostMapping("/upload")
    public UploadedFile uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Please select a file to upload.");
        }
        
        String originalFilename = file.getOriginalFilename();
        
        RandomString rs = new RandomString(16);
        String serverFilename = rs.nextString()+"."+ToolBox.getFileExtension(originalFilename);
        
        
        UploadedFile uf = new UploadedFile();
        uf.setOriginalFilename(originalFilename);
        uf.setServerFilename(serverFilename);
       
        File destinationOnServer = new File(uploadDir, serverFilename);
        file.transferTo(destinationOnServer);
        
        return service.save(uf);
    }
}
