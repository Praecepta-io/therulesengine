package io.praecepta.rules.hub.filebased.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.praecepta.core.helper.PraeceptaObjectHelper;

public class PraeceptaFileUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(PraeceptaFileUtil.class);
	
	public static void createFile(Path filePath){
		try {
			// check for folder exists or not
			if (checkFolderExists(filePath)) {
				if(!Files.exists(filePath)) {
					Files.createFile(filePath);
				}
			}
		} catch (Exception e) {
			LOG.error("error while creating file",e);
		}
	}
	
	public static void renameFile(String currentFileName, String newFileName) {
		
		LOG.info("File to rename from {} to {}: ", currentFileName,newFileName);

		Path sourcePath = Paths.get(currentFileName);
		Path targetPath = Paths.get(newFileName);
		try {
			Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			LOG.error("error while renaming file", e);
		}
	}
	
	public static boolean checkFolderExists(Path filePath){
		try {
			// Create directory for non existed path.
			Path directoryPath=filePath.getParent();
			
			if(Files.exists(directoryPath)) {
				LOG.debug("Directory path already exist {}:", directoryPath.toString());
			}else {
				Files.createDirectories(directoryPath);
				LOG.debug("Successfully created directories {}:", directoryPath.toString());
			}
		} catch (Exception e) {
			LOG.error("error while creating directories ",e);
			return false;
		}
		return true;
	}
	
	public static void renameFilesInDirectoryWithCommonExtenstion(String filePath, String fileNameSuffix) throws Exception{
		Path sourcePath = Paths.get(filePath);
		
//		if(sourcePath.)
	    File[] allContents = sourcePath.toFile().listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	        	renameFilesInDirectoryWithCommonExtenstion(file.getPath(), fileNameSuffix);
	        }
	    }
	    
	    renameFile(filePath, String.join(filePath, fileNameSuffix));
	}
	
	public static List<String> readAllFilesInDirectoryWithAFormat(String filePath, Predicate<String> predictate) throws Exception{
		
		List<String> fileContentList = new ArrayList<>();
		
		List<String> fileNameList = readAllFilesNamesInDirectoryWithAFormat(filePath, predictate);
		
		fileNameList.forEach( eachName -> {
			try {
				fileContentList.add(readFile(new File(filePath + File.separator + eachName)));
			} catch (IOException e) {
				LOG.error("error while Reading File Contents of the file for file name {} with exception {}", eachName, e);
			}
		});
		
		return fileContentList;
	}
	
	public static List<String> readAllFilesNamesInDirectoryWithAFormat(String filePath, Predicate<String> predictate) {
		
		List<String> fileNameList = new ArrayList<>();
		
		Path sourcePath = Paths.get(filePath);
		
//		if(sourcePath.)
		File[] allFiles = sourcePath.toFile().listFiles();
		if (allFiles != null) {
			for (File file : allFiles) {
				
//				if(file.getName().contains(fileFormat)){
				if(predictate != null && predictate.test(file.getName())) {
					fileNameList.add(file.getName());
				}
			}
		}
		
		return fileNameList;
	}
	
	public static List<String> readAllFilesAbsolutPathsInDirectoryWithAFormat(String filePath, Predicate<String> predictate) {
		LOG.info("Inside readAllFilesAbsolutPathsInDirectoryWithAFormat for File Path {}: ", filePath);
		
		List<String> filteredAbsolutePathList = new ArrayList<>();
		
		Path sourcePath = Paths.get(filePath);
		
		// Create Folder and File in that location if it doesn't exist
		createFile(sourcePath);
		
		File[] allFiles = sourcePath.toFile().listFiles();
		if (allFiles != null) {
			for (File file : allFiles) {
				
				LOG.info("Checking whether Predict works for File {} ", file.getName());
				
				if(predictate != null && predictate.test(file.getName())) {
					
					LOG.info("Predictae is success for file name {} ", file.getName());
					filteredAbsolutePathList.add(file.getAbsolutePath());
				}
			}
		}
		
		return filteredAbsolutePathList;
	}
	
	 /*Method to createFile and write data to the file*/
	public static void writeToFile(String fileName, String data) {
    	
    	LOG.debug("inside writeToFile ");
		LOG.info("FileName {}: ", fileName);
		
		Path filePath = Paths.get(fileName);
		
		createFile(filePath);
		BufferedWriter bufferWriter=null;
		try{
			bufferWriter = Files.newBufferedWriter(filePath);
            bufferWriter.write(data);
            bufferWriter.close();
        } catch (Exception e) {
        	LOG.error("error while writing data to file",e);
		}finally {
			if(bufferWriter!=null) {
				try {
					bufferWriter.close();
				} catch (IOException e) {
					LOG.error("error while writing data to file", e);
				}
			}
		}
    }
	
	/**
     * Read content from the file.
     * @param metdDataFile
     * @return
     * @throws IOException
     */
	public static String readFile(File metdDataFile) throws IOException {
		StringBuilder content = new StringBuilder();
		FileReader fr=null;
		try {
			if (metdDataFile.exists()) {
				fr = new FileReader(metdDataFile);
				int nextChar;
				while ((nextChar = fr.read()) != -1) {
					content.append((char) nextChar);
				}
			}
		} catch (Exception e) {
			LOG.error("error while Reading data to file", e);
		}finally {
			if(fr!=null) {
				fr.close();
			}
		}
		
		System.out.println("File Content to return --> "+content + "  For File Path --> "+metdDataFile.getPath());
		return content.toString();
    }

	public static String readFile(String fileAbsolutePath) throws IOException {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(fileAbsolutePath)) {
			
			return readFile(new File(fileAbsolutePath));
		}
		
		return "";
	}

}
