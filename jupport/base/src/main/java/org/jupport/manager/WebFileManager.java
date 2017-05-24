package org.jupport.manager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface WebFileManager extends FileManager{

	public String writeRealFile( MultipartFile multipartFile, String folderName, String prefixFileName) throws IOException;
	
	public String getRealPath(HttpServletRequest request, String path);
	
}
