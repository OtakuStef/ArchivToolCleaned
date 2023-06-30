package at.ebm.ArchivtoolBackend.UnitTests;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import at.ebm.ArchivtoolBackend.entity.DatabaseAttributes;
import at.ebm.ArchivtoolBackend.entity.AttributeId;
import at.ebm.ArchivtoolBackend.repository.AttributeRepository;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@Log4j2
public class AttributeRepositoryTest {
    @Autowired
    private AttributeRepository attributeRespository;
    
    @BeforeAll
    public void setUp() throws Exception {
        DatabaseAttributes attribute1 = new DatabaseAttributes("VPMReference", "testPart01", "01.01", "54441.12340.12345.32154", "31684SDFSDF521SDV65S", LocalDateTime.now(), LocalDateTime.now(), "admin", "Test Part", null, null, 0, null, 0, null, false, false, 0, 0, null, null, null, null, 0, 0, null);
        DatabaseAttributes attribute2 = new DatabaseAttributes("VPMReference", "testPart02", "01.01", "54441.12340.12345.65435", "31684SDF45445LKNL55S", LocalDateTime.now(), LocalDateTime.now(), "admin", "Test Part 2", null, null, 0, null, 0, null, false, false, 0, 0, null, null, null, null, 0, 0, null);
        this.attributeRespository.save(attribute1);
        this.attributeRespository.save(attribute2);
        Assertions.assertNotNull(attribute1.getVname());
        Assertions.assertNotNull(attribute2.getVname());
    }
    @Test
    public void testFetchData(){
        /*Test data retrieval*/
    	AttributeId testAttributeId = new AttributeId("VPMReference","testPart01","01.01");
        DatabaseAttributes attributeA = attributeRespository.findById(testAttributeId).orElse(null);
        Assertions.assertNotNull(attributeA);
        Assertions.assertEquals("admin", attributeA.getOwner());
        log.info("AttributeRepositoryTest finished");
    }
}
