package org.jupport.common;



import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


//Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 8 Download
//Install the file in ${java.home}/jre/lib/security/.

public class AESCipher {

    private static final String ALGORITHM_AES256 = "AES/CBC/PKCS5Padding";
    // ECP, default
    // private static final String ALGORITHM_AES256 = "AES";
    private static final byte[] INITIAL_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;
    private IvParameterSpec iv;

    /**
     * Create AESCipher based on existing {@link Key}
     *
     * @param key Key
     */
    public AESCipher(Key key) {
        this(key.getEncoded());
    }

    /**
     * Create AESCipher based on existing {@link Key} and Initial Vector (iv) in bytes
     *
     * @param key Key
     */
    public AESCipher(Key key, byte[] iv) {
        this(key.getEncoded(), iv);
    }

    /**
     * <p>Create AESCipher using a byte[] array as a key</p>
     * <p/>
     * <p><strong>NOTE:</strong> Uses an Initial Vector of 16 0x0 bytes. This should not be used to create strong security.</p>
     *
     * @param key Key
     */
    public AESCipher(byte[] key) {
        this(key, INITIAL_IV);
    }

    public AESCipher(byte[] key, byte[] iv) {
        this.secretKeySpec = new SecretKeySpec(key, "AES");
        this.iv = new IvParameterSpec(iv);
        this.cipher = null;
        try {
			this.cipher = Cipher.getInstance(ALGORITHM_AES256);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Takes message and encrypts with Key
     *
     * @param message String
     * @return String Base64 encoded
     */
    public String getEncryptedMessage(String message) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

            byte[] encryptedTextBytes = cipher.doFinal(message.getBytes("UTF-8"));

            return new sun.misc.BASE64Encoder().encode(encryptedTextBytes);
            
            //return BaseEncoding.base64().encode(encryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            
        	//throw Throwables.propagate(e);
            
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Takes Base64 encoded String and decodes with provided key
     *
     * @param message String encoded with Base64
     * @return String
     */
    public String getDecryptedMessage(String message) {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

            byte[] encryptedTextBytes = new BASE64Decoder().decodeBuffer(message);
            
            //byte[] encryptedTextBytes = BaseEncoding.base64().decode(message);
            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

            return new String(decryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
            e.printStackTrace();
        } 
        return null;
    }

    /**
     * Get IV in Base64 Encoded String
     *
     * @return String Base64 Encoded
     */
    public String getIV() {
        //return BaseEncoding.base64().encode(iv.getIV());
        return new sun.misc.BASE64Encoder().encode(iv.getIV());
    }

    private Cipher getCipher(int encryptMode) throws InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(encryptMode, getSecretKeySpec(), iv);
        return cipher;
    }

    private SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }


}

