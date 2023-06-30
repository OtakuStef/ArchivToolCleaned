package at.ebm.ArchivtoolBackend.security;

public interface PasswordHandling {
	
	public String encrypt(String plainPassword) throws Exception;
	
	public String decrypt(String encodedPassword) throws Exception;

}
