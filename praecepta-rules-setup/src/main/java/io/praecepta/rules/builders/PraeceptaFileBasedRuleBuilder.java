package io.praecepta.rules.builders;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author rajasrikar
 * Create an Index/MetaData File to have a Simple Key value pairs to capture - Rule space to List<Rule Groups Hash>
 * Create a Folder with Hashing of CompositeKey and Version. that contains the JSON files of both Rule Space and rule groups
 * Fetch also look at the Index File first to make sure entry exist in that file before even going and reading the Files from FOlders
 * Folder Name [Hashing Value] comes from 
 *
 */
public class PraeceptaFileBasedRuleBuilder {
	
	private final static Logger logger = LoggerFactory.getLogger(PraeceptaFileBasedRuleBuilder.class);

	private String filePath;
	
	public static void main(String[] args) {
		
	}
	
	private void writePosition() {
	    File file = new File(filePath);
	    FileWriter writer = null;
	    try {
	      writer = new FileWriter(file);
	      if (file.canWrite() && !file.exists() ) {
	        String ruleSapceJson = getRuleSapceJson();
	        writer.write(ruleSapceJson);
	      }
	    } catch (Throwable t) {
	      logger.error("Failed writing Rule Space File", t);
	    } finally {
	      try {
	        if (writer != null) writer.close();
	      } catch (IOException e) {
	      }
	    }
	  }

	private String getRuleSapceJson() {
		// TODO Auto-generated method stub
		return null;
	}
}
