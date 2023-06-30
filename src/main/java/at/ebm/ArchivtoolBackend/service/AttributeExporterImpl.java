package at.ebm.ArchivtoolBackend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

import at.ebm.ArchivtoolBackend.entity.DatabaseAttributes;
import at.ebm.ArchivtoolBackend.entity.EnoviaAttributes;
import lombok.extern.log4j.Log4j2;
import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.MatrixException;

@Service
@Log4j2
@EnableConfigurationProperties(value = EnoviaAttributes.class)
public class AttributeExporterImpl implements AttributeExporter {

	private final String sepDump="@@@";
	private final String sepRecord="###";
	
	
	@Autowired
	private EnoviaAttributes enoviaAttributes;
	
	@Autowired
	private EnoviaFileHandler enoviaFileHandler;
	
	@Override
	public List<DatabaseAttributes> exportAll(Context ctx, String type) {
		List<Object> basicAttributes = enoviaAttributes.getBasic();
		List<Object> typeAttributes;
		switch (type) {
		case "VPMReference": 
			typeAttributes = enoviaAttributes.getVpmreference();
			break;
		case "VPMRepReference":
			typeAttributes = enoviaAttributes.getVpmrepreference();
			break;
		case "CATPart":
			typeAttributes = enoviaAttributes.getCatpart();
			break;
		case "CATProduct":
			typeAttributes = enoviaAttributes.getCatproduct();
			break;
		case "CATDrawing":
			typeAttributes = enoviaAttributes.getCatdrawing();
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
		
		log.debug(String.format("%s-Attributes to export: %s %s", type, basicAttributes, typeAttributes));
		
		List<String> mqlQueryParameter = buildQueryParameter(type, basicAttributes, typeAttributes);
		String mqlAttributeQuery = buildAttributeQuery(mqlQueryParameter.size()).toString();
		
		log.debug(String.format("MQl Querry: %s, Parameter: %s", mqlAttributeQuery, mqlQueryParameter));
		
		MQLCommand mql=new MQLCommand();
		String res = "";
				
		try {
			mql.executeCommand(ctx, mqlAttributeQuery, mqlQueryParameter);
			res=mql.getResult().trim();

		} catch (MatrixException e) {
			log.error("Error at executing MQL command" + e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error at executing MQL command");
		}
		
		return resultToDatabaseConverter(ctx, type, basicAttributes, typeAttributes, res);
		
		
	}

	@Override
	public void exportSpecific(Context ctx) {
		// TODO Auto-generated method stub
		// Function may be needed in the future
		
	}
	
	private List<String> buildQueryParameter(String type, List<Object> basicAttributes, List<Object> typeAttributes) {
		List<String> mqlQueryParameter = new ArrayList<String>();
		
		mqlQueryParameter.add(type);
		mqlQueryParameter.add("*");
		mqlQueryParameter.add("*");
					
		for(Object basicAttribute : basicAttributes) {
			mqlQueryParameter.add(basicAttribute.toString());
		}
		
		for(Object typeAttribute : typeAttributes) {
			mqlQueryParameter.add("attribute["+typeAttribute.toString()+"]");
		}
		
		mqlQueryParameter.add(sepDump);
		mqlQueryParameter.add(sepRecord);
		
		return mqlQueryParameter;
	}
	
	private StringBuilder buildAttributeQuery(int parameterSize) {
		StringBuilder mqlAttributeQuerySB = new StringBuilder();
		
		mqlAttributeQuerySB.append("temp query bus $1 $2 $3 select");
		
		int attributeCounter = 4; 
		int fixParameterAmount = 2; //sepDump, sepRec
		while (attributeCounter <= parameterSize-fixParameterAmount) {
			
			mqlAttributeQuerySB.append(" $").append(attributeCounter);
			attributeCounter++;
		}
		
		mqlAttributeQuerySB.append(" dump $").append(parameterSize-1);
		mqlAttributeQuerySB.append(" recordseparator $").append(parameterSize);
		
		
		return mqlAttributeQuerySB;
	}
	
	private List<DatabaseAttributes> resultToDatabaseConverter(Context ctx, String type, List<Object> basicAttributes, List<Object> typeAttributes, String stringResult){
		List<DatabaseAttributes> databaseAttributeList = new ArrayList<DatabaseAttributes>();
		for (String line : stringResult.split(sepRecord)) {
			Map<String, String> attributeMap = new HashMap<String, String>();
			int arraySize = 3 + basicAttributes.size() + typeAttributes.size();
			String[] attributeInput = line.split(sepDump);
			List<String> attributes = new  ArrayList<String>();
			
			Collections.addAll(attributes, attributeInput);

			
			while (attributes.size() < arraySize) {
				attributes.add("");
			}
			
			int attributeCounter = 1;
			
			attributeMap.put("type", type);
			String name = attributes.get(attributeCounter++);
			String revision = attributes.get(attributeCounter++);
			attributeMap.put("name", name);
			attributeMap.put("revision", revision);
			
			for(Object basicAttribute : basicAttributes) {
				attributeMap.put(basicAttribute.toString(), attributes.get(attributeCounter++));
				
			}
			
			for(Object typeAttribute : typeAttributes) {
				attributeMap.put(typeAttribute.toString(), attributes.get(attributeCounter++));
				
			}
			
			attributeMap.put("filelocation", enoviaFileHandler.getFilePath(ctx, type, name, revision));
			
			databaseAttributeList.add(new DatabaseAttributes().toDatabaseAttribute(attributeMap));
		}
		
		return databaseAttributeList;
	}
	

}
