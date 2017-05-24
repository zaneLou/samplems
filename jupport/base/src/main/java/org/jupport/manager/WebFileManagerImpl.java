package org.jupport.manager;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.web.bind.ServletRequestUtils;

@Component
public class WebFileManagerImpl extends FileManagerImpl implements WebFileManager 
{

	@Override
	public String writeRealFile( MultipartFile multipartFile, String folderName, String prefixFileName) throws IOException 
	{
		String originalFilename = multipartFile.getOriginalFilename();
		String fileNameSuffix = "";
		int suffixIndexOf = originalFilename.lastIndexOf(".");
		if (suffixIndexOf > -1) {
			fileNameSuffix = originalFilename.substring(suffixIndexOf);
		}
		String fileName = prefixFileName+fileNameSuffix;
		byte[] bytes = multipartFile.getBytes();
		//long size = multipartFile.getSize();
		writeFile( folderName, fileName, bytes);
		return fileName;
	}
	
	@Override
	public String getRealPath(HttpServletRequest request, String path) {
		ServletContext sc = request.getSession().getServletContext();
		return sc.getRealPath(path);
	}
}
