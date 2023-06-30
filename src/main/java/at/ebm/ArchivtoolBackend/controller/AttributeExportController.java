package at.ebm.ArchivtoolBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ebm.ArchivtoolBackend.entity.DatabaseAttributes;
import at.ebm.ArchivtoolBackend.repository.AttributeRepository;
import at.ebm.ArchivtoolBackend.security.PasswordHandling;
import at.ebm.ArchivtoolBackend.service.AttributeExporter;
import at.ebm.ArchivtoolBackend.service.EnoviaConnection;
import at.ebm.ArchivtoolBackend.service.EnoviaFileHandler;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import lombok.AccessLevel;

@RestController
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AttributeExportController {
	EnoviaConnection enoviaConnection;
	AttributeExporter attributeExporter;
	PasswordHandling passwordHandling;
	EnoviaFileHandler enoviaFileHandler;
	
    @Autowired
    private AttributeRepository attributeRespository;
	
    @GetMapping("/export")
    public ResponseEntity<Void> exportAttributes(@RequestParam String type) {
    	List<DatabaseAttributes> attributeExport = attributeExporter.exportAll(enoviaConnection.connectToClient(), type);
    	saveToDatabase(attributeExport);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<DatabaseAttributes>> search(@RequestParam String attribute, @RequestParam String search, @RequestParam(defaultValue = "false") boolean preciseSearch){
    	
    	List<DatabaseAttributes> searchRes = null;
    	if (preciseSearch) {
			searchRes = findByPreciseAttributeSearch(attribute, search);	
		}else {
			searchRes = findByAttributeSearch(attribute, search);			
		}
    	
    	if (searchRes.isEmpty()) {
    		return new ResponseEntity<List<DatabaseAttributes>>(HttpStatus.OK);
		} else {
			return new ResponseEntity<List<DatabaseAttributes>>(searchRes, HttpStatus.OK);
		}
    }
    
    @GetMapping("/encrypt")
    public String passwordEncryption(@RequestParam(value = "password") String plainPassword){
    	
    	try {
    		return passwordHandling.encrypt(plainPassword);
			
		} catch (Exception e) {
			return null;
		}
		
    	
    }
    
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "type") String type, @RequestParam(value = "name") String name, @RequestParam(value = "revision") String revision) {
    	Resource file = enoviaFileHandler.downloadFile(type, name, revision);
    	    	
    	return ResponseEntity.ok()
    			.contentType(MediaType.APPLICATION_OCTET_STREAM)
    			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + getFileEnding(type) + "\"")
    			.body(file);
    }
    
    private void saveToDatabase(List<DatabaseAttributes> databaseAttributes) {
    	attributeRespository.saveAll(databaseAttributes);
    }
    
    private List<DatabaseAttributes> findByAttributeSearch(String attribute, String searchValue) {
    	List<DatabaseAttributes> searchRes = null;
    	
    	switch (attribute) {
		case "name":
			searchRes = attributeRespository.findByNameContaining(searchValue);
			break;

		case "owner":
			searchRes = attributeRespository.findByOwnerContaining(searchValue);
			break;
			
		case "vname":
			searchRes = attributeRespository.findByVnameContaining(searchValue);
			break;
			
		case "projectnumber":
			searchRes = attributeRespository.findByEbmprojectnumberContaining(searchValue);
			break;

		default:
			log.warn("Attribute not found: " + searchValue);
			break;
		}
    	
    	return searchRes;
    }
    
    private List<DatabaseAttributes> findByPreciseAttributeSearch(String attribute, String searchValue) {
    	List<DatabaseAttributes> searchRes = null;
    	
    	switch (attribute) {
		case "name":
			searchRes = attributeRespository.findByName(searchValue);
			break;

		case "owner":
			searchRes = attributeRespository.findByOwner(searchValue);
			break;
			
		case "vname":
			searchRes = attributeRespository.findByVname(searchValue);
			break;
		
		case "projectnumber":
			searchRes = attributeRespository.findByEbmprojectnumber(searchValue);
			break;

		default:
			log.warn("Attribute not found: " + searchValue);
			break;
		}
    	
    	return searchRes;
    }
    
    private String getFileEnding(String type) {
    	String filetype = ".unknown";
    	
    	switch (type) {
		case "CATPart", "CATDrawing", "CATProduct":
			filetype = "." + type;
			break;

		default:
			break;
		}
    	
    	return filetype;
    }
}
