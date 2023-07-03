package at.ebm.ArchivtoolBackend.service;

import at.ebm.ArchivtoolBackend.entity.DatabaseAttributes;
import at.ebm.ArchivtoolBackend.entity.EnoviaAttributes;
import lombok.SneakyThrows;
import matrix.db.Context;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AttributeExporterImplTest {

    @InjectMocks
    AttributeExporterImpl attributeExporter;

    @Mock
    EnoviaAttributes enoviaAttributes;

    @Mock
    EnoviaFileHandler enoviaFileHandler;

    @Mock
    DatabaseAttributes databaseAttributes;



    private Context context;
    private String minValidQueryAttributes = "temp query bus $1 $2 $3 select dump $4 recordseparator $5";
    private String testType = "VPMReference";
    private List<Object> mockedBasicAttribute = List.of("basic");
    private List<Object> mockedTypeAttribute = List.of("type");

    @BeforeEach
    @SneakyThrows
    void setup(){
        context = new Context("");
    }



    @Test
    void correct_query_parameter(){
        List<String> testQueryParameter = attributeExporter.buildQueryParameter(testType,mockedBasicAttribute,mockedTypeAttribute);

        Assertions.assertEquals(testType,testQueryParameter.get(0));
        Assertions.assertEquals(mockedBasicAttribute.get(0).toString(),testQueryParameter.get(3));
        Assertions.assertEquals("attribute["+mockedTypeAttribute.get(0).toString()+"]",testQueryParameter.get(4));
    }

    @Test
    void correct_query_attributes(){
        int minValidLength = 5;
        String expectedValidQuery = minValidQueryAttributes;
        String attributeQuery = attributeExporter.buildAttributeQuery(minValidLength).toString();
        Assertions.assertEquals(expectedValidQuery,attributeQuery);
    }

    @Test
    void invalid_length_query_attributes(){
        int invalidLength = 4;
        String expected = "";
        String attributeQuery = attributeExporter.buildAttributeQuery(invalidLength).toString();
        Assertions.assertEquals(expected,attributeQuery);
    }
    
    @Test
    @SneakyThrows
    void invalid_VPMReference_exportAll(){
        when(enoviaAttributes.getVpmreference()).thenReturn(List.of("test"));

        Assertions.assertThrows(ResponseStatusException.class, () -> attributeExporter.exportAll(context,testType));
    }

    @Test
    void throw_invalid_result(){
        String testMQLResult = "type@@@name@@@revision@@@basic@@@type###type@@@name2@@@revision@@@basic@@@type";
        doReturn("").when(enoviaFileHandler).getFilePath(any(),anyString(),anyString(),anyString());

        Assertions.assertThrows(NullPointerException.class,() -> attributeExporter.resultToDatabaseConverter(context,testType,mockedBasicAttribute,mockedTypeAttribute,testMQLResult));
    }

}