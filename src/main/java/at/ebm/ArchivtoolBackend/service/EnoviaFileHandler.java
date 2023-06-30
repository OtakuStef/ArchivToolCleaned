package at.ebm.ArchivtoolBackend.service;

import org.springframework.core.io.Resource;

import matrix.db.Context;

public interface EnoviaFileHandler {
	
	public String getFilePath(Context ctx, String type, String name, String revision);
	
	public Resource downloadFile(String type, String name, String revision);

}
