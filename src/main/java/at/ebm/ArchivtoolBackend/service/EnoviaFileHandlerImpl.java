package at.ebm.ArchivtoolBackend.service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import at.ebm.ArchivtoolBackend.entity.DatabaseAttributes;
import at.ebm.ArchivtoolBackend.config.ExperienceConnectionConfig;
import at.ebm.ArchivtoolBackend.entity.AttributeId;
import at.ebm.ArchivtoolBackend.repository.AttributeRepository;
import lombok.extern.log4j.Log4j2;
import matrix.db.Context;
import matrix.db.MQLCommand;
import matrix.util.MatrixException;

@Service
@Log4j2
public class EnoviaFileHandlerImpl implements EnoviaFileHandler {

	@Autowired
	private ExperienceConnectionConfig experienceConnectionConfig;
	
    @Autowired
    private AttributeRepository attributeRespository;
	
	private String sepDump = "@@@";
	
	@Override
	public String getFilePath(Context ctx, String type, String name, String revision) {
		
		String mqlQuery = "temp query bus $1 $2 $3 select $4 $5 $6 dump $7";
		List<String> mqlQueryParameter = List.of(type, name, revision, "format.hasfile", "format.file.store", "format.file.capturedfile", sepDump);
		String filePathData = getFilePathData(ctx, mqlQuery, mqlQueryParameter);
			
		return buildFilePath(filePathData);
	}

	@Override
	public Resource downloadFile(String type, String name, String revision) {
		AttributeId attributeId = new AttributeId(type, name, revision);
		Optional<DatabaseAttributes> searchedFile = attributeRespository.findById(attributeId);
		
		if (searchedFile.isPresent()) {
			String filePath = searchedFile.get().getFileLocation();

			Resource file = new FileSystemResource(filePath);
			
			
			if (file.exists()) {
				return file;
			} else {
				log.error("File not found " + filePath);
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Not found");
			}
				
			
		} else {
			log.error(String.format("File could not be found %s %s %s", type, name, revision));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error at FileSearch for Download");
		}

	}
	
	private String getFilePathData(Context ctx, String mqlQuery, List<String>mqlParameter) {
		String res = "";		
		MQLCommand mql=new MQLCommand();
		
		try {
			mql.executeCommand(ctx, mqlQuery, mqlParameter);
			res=mql.getResult().trim();

		} catch (MatrixException e) {
			log.error("Error at executing MQL command" + e.getMessage());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error at executing MQL command");
		}
				
		return res;
	}
	
	private String buildFilePath(String filePathData) {
		String[] data = filePathData.split(sepDump);
		
		String storePath = experienceConnectionConfig.getFileStorage();
		boolean hasFile = Boolean.valueOf(data[3]);
		
		if (hasFile) {
			String storeName = data[4];	
			String inStoreFilePath = data[5].replace("/", "\\");
			return String.format("%s\\%s\\%s", storePath, storeName, inStoreFilePath);
		} else {
			return null;
		}

	}

}
