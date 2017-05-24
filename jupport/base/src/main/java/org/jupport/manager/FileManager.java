package org.jupport.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public interface FileManager {

	public String getyyMMWTypeddHH(String type, Date now);
	public String getFineName(String originalFilename, String prefixFileName);
	public int writeFile(String folderName, String fileName, byte[] bytes) throws FileNotFoundException, IOException;
	public int writeFile(String folderName, String fileName, InputStream inputStream) throws FileNotFoundException, IOException;
	public boolean deleteFile(String filename);
	public boolean move(File fromFile, File toFile);
	public boolean copy(File fromFile, File toFile);
	
	public void appendFile(String source, String target);
	public int splitFile(String source, String partname, int partSize);
	
	public String getImageSize(String picture, String conn);
}
