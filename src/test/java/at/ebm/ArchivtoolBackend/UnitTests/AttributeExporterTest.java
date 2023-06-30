package at.ebm.ArchivtoolBackend.UnitTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import at.ebm.ArchivtoolBackend.service.AttributeExporter;
import lombok.extern.log4j.Log4j2;
import matrix.db.Context;
import matrix.util.MatrixException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@Log4j2
public class AttributeExporterTest {
	private Context ctx;
	
	@Autowired
	private AttributeExporter attributeExporter;
	
	@BeforeAll
	public void setUp() throws MatrixException {
		ctx = new Context("");
	}
	
	@Test
	public void testAttributeExport() {

		try {
			attributeExporter.exportAll(ctx, "VPMReference");
			log.info("Export Successful");
		} catch (Exception e) {
			log.info("Export failed");
		}
	}

}
