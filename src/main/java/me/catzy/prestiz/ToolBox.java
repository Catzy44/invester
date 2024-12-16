package me.catzy.prestiz;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.catzy.prestiz.objects.grupy.GrupyService;

@Service("ToolBox")
@Transactional
public class ToolBox {
	private static GrupyService service;

	@Autowired
	private ProjectionFactory projFac;

	@Autowired
	private RepositoryRestConfiguration restConf;

	@Autowired
	public void setService(GrupyService service) {
		ToolBox.service = service;
	}

	public static GrupyService getService() {
		return service;
	}

	public List<?> projectList(List<?> list, String projection) {
		if (projection == null)
			return list;
		if (list.size() == 0)
			return list;
		Class<?> typeOfOriginalObject = list.get(0).getClass();
		Class<?> type = this.restConf.getProjectionConfiguration().getProjectionType(typeOfOriginalObject, projection);
		if (type == null)
			return list;
		return (List) list.stream().map(s -> this.projFac.createProjection(type, s)).collect(Collectors.toList());
	}

	public Object projectObj(Object object, String projection) {
		if (projection == null)
			return object;
		Class<?> typeOfOriginalObject = object.getClass();
		Class<?> type = this.restConf.getProjectionConfiguration().getProjectionType(typeOfOriginalObject, projection);
		if (type == null)
			return object;
		return this.projFac.createProjection(type, object);
	}

	public static String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf(".");
		if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
			return ""; // No extension found
		}
		return fileName.substring(lastDotIndex + 1);
	}
	public static String hashSHA256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(input.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encodedHash);
    }

	public static String generateAuthmeString(String password) throws Exception {
		RandomString rs = new RandomString(16);
		
		String salt = rs.nextString();
		String doubleHashedPassword = hashSHA256(hashSHA256(password) + salt);
		return "$SHA$" + salt + "$" + doubleHashedPassword;
	}
}
