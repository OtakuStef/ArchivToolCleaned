package at.ebm.ArchivtoolBackend.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attribute_tbl")
@IdClass(AttributeId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseAttributes {
    @Id
    @Column(name = "type", nullable = false)
    private String type;
    
    @Id
    @Column(name = "name", nullable = false)
    private String name;
    
    @Id
    @Column(name = "revision", nullable = false)
    private String revision;
    
    @Column(name = "enovia_id")
    private String enoviaId;
    
    @Column(name = "physical_id")
    private String pyhsicalId;
    
    @Column(name = "originated")
    private LocalDateTime originated;
    
    @Column(name = "modified")
    private LocalDateTime modified;
    
    @Column(name = "owner")
    private String owner;
    
    @Column(name = "v_name")
    private String vname;
    
    @Column(name = "file_location")
    private String fileLocation;
    
    @Column(name = "ebm_coating")
    private String ebmcoating;
    
    @Column(name = "ebm_diameter")
    private float ebmdiameter;
    
    @Column(name = "ebm_espritnode")
    private String ebmespritnode;
    
    @Column(name = "ebm_height")
    private float ebmheight;
    
    @Column(name = "ebm_identnumber")
    private String ebmidentnumber;
    
    @Column(name = "ebm_ismethodplan")
    private boolean ebmismethodplan;
    
    @Column(name = "ebm_isstandartpart")
    private boolean ebmisstandartpart;
    
    @Column(name = "ebm_length")
    private float ebmlength;
    
    @Column(name = "ebm_mass")
    private float ebmmass;
    
    @Column(name = "ebm_materialcode")
    private String ebmmaterialcode;
    
    @Column(name = "ebm_ordernumber")
    private String ebmordernumber;
    
    @Column(name = "ebm_previousowner")
    private String ebmpreviousowner;
    
    @Column(name = "ebm_projectnumber")
    private String ebmprojectnumber;
    
    @Column(name = "ebm_wallthickness")
    private float ebmwallthickness;
    
    @Column(name = "ebm_width")
    private float ebmwidth;
    
    @Column(name = "ebm_heattreatment")
    private String ebmheattreatment;


    
    public DatabaseAttributes toDatabaseAttribute(Map<String, String> attributeExport) {
    	if(attributeExport == null) {
    		return null;
    	}
    	
    	DatabaseAttributes databaseAttribute = new DatabaseAttributes();
    	databaseAttribute.setType(attributeExport.get("type"));
    	databaseAttribute.setName(attributeExport.get("name"));
    	databaseAttribute.setRevision(attributeExport.get("revision"));
    	databaseAttribute.setEnoviaId(attributeExport.get("id"));
    	databaseAttribute.setOriginated(strToDate(attributeExport.get("originated")));
    	databaseAttribute.setModified(strToDate(attributeExport.get("modified")));
    	databaseAttribute.setPyhsicalId(attributeExport.get("physicalid"));
    	databaseAttribute.setOwner(attributeExport.get("owner"));
    	databaseAttribute.setVname(attributeExport.get("PLMEntity.V_Name"));
    	databaseAttribute.setFileLocation(attributeExport.get("filelocation"));
    	databaseAttribute.setEbmcoating(attributeExport.get("EBMCoating"));
    	databaseAttribute.setEbmdiameter(strToFloat(attributeExport.get("EBMDiameter")));
    	databaseAttribute.setEbmespritnode(attributeExport.get("EBMEspritNode"));
    	databaseAttribute.setEbmheight(strToFloat(attributeExport.get("EBMHeight")));
    	databaseAttribute.setEbmidentnumber(attributeExport.get("EBMIdentNumber"));
    	databaseAttribute.setEbmismethodplan(strToBool(attributeExport.get("EBMIsMethodPlan")));
    	databaseAttribute.setEbmisstandartpart(strToBool(attributeExport.get("EBMIsStandartPart")));
    	databaseAttribute.setEbmlength(strToFloat(attributeExport.get("EBMLength")));
    	databaseAttribute.setEbmmass(strToFloat(attributeExport.get("EBMMass")));
    	databaseAttribute.setEbmmaterialcode(attributeExport.get("EBMMaterialCode"));
    	databaseAttribute.setEbmordernumber(attributeExport.get("EBMOrderNumber"));
    	databaseAttribute.setEbmpreviousowner(attributeExport.get("EBMPreviousOwner"));
    	databaseAttribute.setEbmprojectnumber(attributeExport.get("EBMProjectNumber"));
    	databaseAttribute.setEbmwallthickness(strToFloat(attributeExport.get("EBMWallThickness")));
    	databaseAttribute.setEbmwidth(strToFloat(attributeExport.get("EBMWidth")));
    	databaseAttribute.setEbmheattreatment(attributeExport.get("EBMHeatTreatment"));
    	return databaseAttribute;

    }
    
    private LocalDateTime strToDate(String dateString) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:m:s a");
    	return LocalDateTime.parse(dateString, formatter);
    }
    
    public static float strToFloat(String str) {
    	if (str == null || str.isEmpty()) {
			return 0.0f;
		}
        return Float.valueOf(str);
      }
    
    public static boolean strToBool(String str) {
        return Boolean.valueOf(str);
      }
        
}

