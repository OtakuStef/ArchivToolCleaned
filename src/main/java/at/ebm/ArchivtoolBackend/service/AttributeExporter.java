package at.ebm.ArchivtoolBackend.service;

import java.util.List;

import at.ebm.ArchivtoolBackend.entity.DatabaseAttributes;
import matrix.db.Context;

public interface AttributeExporter {

	public List<DatabaseAttributes> exportAll(Context ctx, String type);
	
	public void exportSpecific(Context ctx);
}
