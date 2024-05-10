package io.praecepta.rules.sidecars.parser.impl;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.praecepta.core.helper.PraeceptaObjectHelper;
import io.praecepta.rules.sidecars.to.PraeceptaSideCarDataHolder;

public class PraeceptaXmlParser extends PraeceptaJsonParser{
	
	private static final XmlMapper xmlMapper = new XmlMapper();
	
	private  static final ObjectMapper jsonMapper = new ObjectMapper();

	@Override
	public void parse(PraeceptaSideCarDataHolder<String, Map<String, Object>> sideCarDataHolder) {
		
		if(!PraeceptaObjectHelper.isObjectEmpty(sideCarDataHolder)) {
			
			String inputXml = sideCarDataHolder.retriveInput();
			
			if(!PraeceptaObjectHelper.isObjectEmpty(inputXml)) {
				
				try {
					JsonNode node = xmlMapper.readTree(inputXml.getBytes());
					
					if(node != null) {
						String json = jsonMapper.writeValueAsString(node);
						
						if(!PraeceptaObjectHelper.isObjectEmpty(json)) {
							
							sideCarDataHolder.addInput(json);
							super.parse(sideCarDataHolder);
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
}
