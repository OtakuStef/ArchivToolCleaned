package at.ebm.ArchivtoolBackend.entity;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "enovia-attributes")
@Getter
@Setter
public class EnoviaAttributes {

	private List<Object> basic;
	private List<Object> vpmreference;
	private List<Object> vpmrepreference;
	private List<Object> catpart;
	private List<Object> catproduct;
	private List<Object> catdrawing;
	
}
