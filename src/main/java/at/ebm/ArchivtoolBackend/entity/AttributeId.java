package at.ebm.ArchivtoolBackend.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttributeId implements Serializable{
	private String type;
	private String name;
	private String revision;
	
}
