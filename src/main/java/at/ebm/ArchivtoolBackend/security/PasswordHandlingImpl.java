package at.ebm.ArchivtoolBackend.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import jakarta.xml.bind.DatatypeConverter;

@Service
public class PasswordHandlingImpl implements PasswordHandling {
	
	//decryption password
    private static String KeyString = "ebm";
    
    private static byte[] get128BitKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
    	MessageDigest sha = MessageDigest.getInstance("SHA-1");
		byte[] key = sha.digest(password.getBytes("UTF-8"));
		key = Arrays.copyOf(key, 16);
    	return key;
    }

	@Override
	public String encrypt(String plainPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(get128BitKey(KeyString), "AES"));
		byte[] ciphertext = cipher.doFinal(plainPassword.getBytes("UTF-8"));

		return DatatypeConverter.printBase64Binary(ciphertext);
	}

	@Override
	public String decrypt(String encodedPassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(get128BitKey(KeyString), "AES"));
		byte[] ciphertext = cipher.doFinal(DatatypeConverter.parseBase64Binary(encodedPassword));

		return new String(ciphertext);
	}

}
