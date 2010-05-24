package com.atlas.fun.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.binary.Base64;

public class EnDeClip {
	private static String algorithm = "DESede";
	private static Cipher cipher = null;

	public static void setUp() throws Exception {
        cipher = Cipher.getInstance(algorithm);
    }

	public static Key makeKey()throws Exception {
        return javax.crypto.KeyGenerator.getInstance(algorithm).generateKey();
	}

	public static String encrypt(String input, Key key) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = input.getBytes();
		return new String(Base64.encodeBase64(cipher.doFinal(inputBytes)));
	}

	public static String decrypt(String encryption, Key key)
			throws InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException {
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = cipher.doFinal(Base64.decodeBase64(encryption.getBytes()));
		String recovered = new String(recoveredBytes);
		return recovered;
	}

	public static void main(String[] args) throws Exception {
		String data = "안녕";

		Key key = EnDeClip.makeKey();

		EnDeClip.setUp();

		String enc = encrypt(data, key);
		String dec = decrypt(enc, key);

		System.out.println(data.equals(dec));

		FileOutputStream out = new FileOutputStream("d:/key.sec");
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(key);
		out.close();

		FileInputStream in = new FileInputStream("d:/key.sec");
		ObjectInputStream ois = new ObjectInputStream(in);
		Key marshalKey = (Key) ois.readObject();
		in.close();

		enc = encrypt(data, marshalKey);
		dec = decrypt(enc, marshalKey);

		System.out.println(data.equals(dec));

	}
}
