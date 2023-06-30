package at.ebm.ArchivtoolBackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("experience-login")
@Getter
@Setter
public class ExperienceConnectionConfig {

	private boolean usePassport;
	private String host;
	private String user;
	private String password;
	private String securityContext;
	private String fileStorage;
	
	
}
