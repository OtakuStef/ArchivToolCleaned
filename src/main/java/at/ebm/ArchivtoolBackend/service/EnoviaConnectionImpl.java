package at.ebm.ArchivtoolBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import at.ebm.ArchivtoolBackend.config.ExperienceConnectionConfig;
import at.ebm.ArchivtoolBackend.security.PasswordHandling;
import lombok.extern.log4j.Log4j2;
import matrix.db.Context;

@Service
@Log4j2
public class EnoviaConnectionImpl implements EnoviaConnection{

	@Autowired
	private ExperienceConnectionConfig experienceConnectionConfig;
	
	@Autowired
	private PasswordHandling passwordHandling;
	
	private static Context ctx=null;
	
	@Override
	public Context connectToClient() {
		try {
			
			String host = experienceConnectionConfig.getHost();
			String user = experienceConnectionConfig.getUser();
			String password = passwordHandling.decrypt(experienceConnectionConfig.getPassword());
			String role = experienceConnectionConfig.getSecurityContext();
			
			if(experienceConnectionConfig.isUsePassport()) {
				String ticket = PassportImpl.getTicket(host, user, password);
				ctx=new Context(host+ticket);
			} else {
				ctx=new Context(host);
				ctx.setPassword(password);
			}			
			ctx.setUser(user);
			ctx.setRole(role);
			ctx.connect();
			
		}catch(Exception e){
			log.error("Passport connection failed. Error: " + e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passport connection failed");
		}
		
		
		return ctx;
				
	}
}
