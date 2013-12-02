/**
 * 
 */
package com.adgistics.com.metora.stores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.io.FilenameUtils;

/**
 * @author cjcartlidge
 *
 */
public class DiskStorage implements Storage {

	private final File rootDirectory;
	
	public DiskStorage(String root, String domain){
		rootDirectory = new File(FilenameUtils.concat(root, domain));
		if (rootDirectory.exists() == false){
			rootDirectory.mkdir();
		}	
	}
	
	@Override
	public Boolean add(String fileName, byte[] content) {
		
		Boolean result = true;
		
		try{
			String path = FilenameUtils.concat(rootDirectory.getAbsolutePath(), fileName);
			FileOutputStream fos = new FileOutputStream(path);
	    	fos.write(content);
	    	fos.close();
		}
		catch(Exception exception){
			result = false;
		}
		
		return result;
	}

	@Override
	public InputStream get(String fileName) {
		
		InputStream result = null;
		
		try{
			String path = FilenameUtils.concat(rootDirectory.getAbsolutePath(), fileName);
			result = new FileInputStream(path);
		}
		catch(Exception exception){
			
		}
		
		return result;
	}
}
