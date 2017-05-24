package org.jupport.manager;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class FileManagerImpl implements FileManager{

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public String getFineName(String originalFilename, String prefixFileName)
	{
		String fileNameSuffix = "";
		int suffixIndexOf = originalFilename.lastIndexOf(".");
		if (suffixIndexOf > -1) {
			fileNameSuffix = originalFilename.substring(suffixIndexOf);
		}
		return prefixFileName+fileNameSuffix.toLowerCase();
	}
	
	
	@Override
	public String getyyMMWTypeddHH(String type, Date now) 
	{
		// set RelativePath,add dateFolder /YY/MM/dd/mm
		SimpleDateFormat df = new SimpleDateFormat("yy/MM/W/");
		String dateFolder = df.format(now);
		df = new SimpleDateFormat("/dd/HH/");
		dateFolder = dateFolder + type + df.format(now);
		return dateFolder;
	}
	
	@Override
	public int writeFile(String folderName, String fileName, byte[] bytes) throws FileNotFoundException, IOException 
	{
		// mkdirs
		new File(folderName).mkdirs();
		// save image file
		FileOutputStream out = new FileOutputStream(folderName+"/"+fileName);
		BufferedOutputStream bOut = new BufferedOutputStream(out);
		bOut.write(bytes);
		bOut.flush();
		bOut.close();
		
		logger.info("writeFile at folder :" + folderName + ", fileName: " + fileName);
		// set size
		return bytes.length;
	}
	
	@Override
	public int writeFile(String folderName, String fileName, InputStream inputStream) throws FileNotFoundException, IOException 
	{
		// mkdirs
		new File(folderName).mkdirs();
		// save image file
		FileOutputStream out = new FileOutputStream(folderName+"/"+fileName);
		BufferedOutputStream bOut = new BufferedOutputStream(out);
		
		int bytesWritten = 0;
		int byteCount = 0;
		byte[] bytes = new byte[1024*64];
		while ((byteCount = inputStream.read(bytes)) != -1)
		{
			bOut.write(bytes, 0, byteCount);
			bytesWritten += byteCount;
		}
	    inputStream.close();  
		bOut.flush();
		bOut.close();
		
		logger.info("writeFile at folder :" + folderName + ", fileName: " + fileName);
		// set size
		return bytesWritten;
	}

	@Override
	public boolean deleteFile(String filename) 
	{
		File file = new File(filename);
		if (file.exists()) {
			if (logger.isInfoEnabled()) {
				logger.info("Class FileManagerImpl delete file :" + filename);
			}
			return file.delete();
		} else {
			return false;
		}
	}
	
	/**
	 * Copy a File The renameTo method does not allow action across NFS mounted filesystems this
	 * method is the workaround
	 * 
	 * @param fromFile
	 *            The existing File
	 * @param toFile
	 *            The new File
	 * @return <code>true</code> if and only if the renaming succeeded; <code>false</code> otherwise
	 */
	@Override
	public boolean copy(File fromFile, File toFile) {
		try {
			FileInputStream in = new FileInputStream(fromFile);
			FileOutputStream out = new FileOutputStream(toFile);
			BufferedInputStream inBuffer = new BufferedInputStream(in);
			BufferedOutputStream outBuffer = new BufferedOutputStream(out);

			int theByte = 0;

			while ((theByte = inBuffer.read()) > -1) {
				outBuffer.write(theByte);
			}
			outBuffer.close();
			inBuffer.close();
			out.close();
			in.close();
			// cleanupif files are not the same length
			if (fromFile.length() != toFile.length()) {
				toFile.delete();
				return false;
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Move a File The renameTo method does not allow action across NFS mounted filesystems this
	 * method is the workaround
	 * 
	 * @param fromFile
	 *            The existing File
	 * @param toFile
	 *            The new File
	 * @return <code>true</code> if and only if the renaming succeeded; <code>false</code> otherwise
	 */
	@Override
	public boolean move(File fromFile, File toFile) {
		if (fromFile.renameTo(toFile)) {
			return true;
		}

		// delete if copy was successful, otherwise move will fail
		if (copy(fromFile, toFile)) {
			return fromFile.delete();
		}

		return false;
	}
	
	@Override
	public void appendFile(String source, String target)
	{
		try {
			File sourceFile = new File(source);
			File targetFile = new File(target);
			if(sourceFile.exists())
			{
				FileInputStream in = new FileInputStream(sourceFile);
				FileOutputStream out = new FileOutputStream(targetFile, true);
				byte[] buf = new byte[8192];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
			else
			{
				logger.error("No Found File:" + source);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@Override
	public int splitFile(String source, String partname, int partSize) 
	{
		File inputFile = new File(source);
		FileInputStream inputStream;
		String newFileName;
		FileOutputStream filePart;
		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = partSize;
		byte[] byteChunkPart;
		try {
			inputStream = new FileInputStream(inputFile);
			while (fileSize > 0) {
				if (fileSize <= readLength) {
					readLength = fileSize;
				}
				byteChunkPart = new byte[readLength];
				read = inputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);
				nChunks++;
				newFileName = source + "." + partname + Integer.toString(nChunks - 1);
				filePart = new FileOutputStream(new File(newFileName));
				filePart.write(byteChunkPart);
				filePart.flush();
				filePart.close();
				byteChunkPart = null;
				filePart = null;
			}
			inputStream.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return nChunks;
	}
	 
	@Override
	public String getImageSize(String picture, String conn){
		BufferedImage sourceImg;
		try {
			sourceImg = ImageIO.read(new FileInputStream(picture));
			return sourceImg.getWidth() + conn +  sourceImg.getHeight();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		return "";
	}
}
