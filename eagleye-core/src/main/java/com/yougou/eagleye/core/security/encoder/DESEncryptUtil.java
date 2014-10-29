package com.yougou.eagleye.core.security.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class DESEncryptUtil {
	public static Key createKey() throws NoSuchAlgorithmException {
		Security.insertProviderAt(new com.sun.crypto.provider.SunJCE(), 1);
		KeyGenerator generator = KeyGenerator.getInstance("DES");
		generator.init(new SecureRandom());
		Key key = generator.generateKey();
		return key;
	}
	
	public static Key getKey(InputStream is) {
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			return (Key) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	private static byte[] doEncrypt(Key key, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] raw = cipher.doFinal(data);
			return raw;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static InputStream doDecrypt(Key key, InputStream in) {
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] tmpbuf = new byte[1024];
			int count = 0;
			while ((count = in.read(tmpbuf)) != -1) {
				bout.write(tmpbuf, 0, count);
				tmpbuf = new byte[1024];
			}
			in.close();

			byte[] orgData = bout.toByteArray();
			byte[] raw = cipher.doFinal(orgData);

			ByteArrayInputStream bin = new ByteArrayInputStream(raw);
			return bin;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		//解密
//		args = new String[]{
//				"decrypt",
//				"D:\\en_car.properties",
//				"D:\\key.dat"};
		//加密
		args = new String[]{
				"encrypt",
				"D:\\db.properties",
				"D:\\key.dat"};
		//生成密匙文件
//		args = new String[]{
//				"key",
//				"d:\\key.dat"};
		if (args.length == 2 && args[0].equals("key")) {// 生成密钥文件
			Key key = DESEncryptUtil.createKey();
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(args[1]));
			oos.writeObject(key);
			oos.close();
			System.out.println("成功生成密钥文件。");
		} else if (args.length == 3 && args[0].equals("encrypt")) {//对文件进行加密
			File file = new File(args[1]);
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] tmpbuf = new byte[1024];
			int count = 0;
			while ((count = in.read(tmpbuf)) != -1) {
				bout.write(tmpbuf, 0, count);
				tmpbuf = new byte[1024];
			}
			in.close();
			byte[] orgData = bout.toByteArray();
			Key key = getKey(new FileInputStream(args[2]));
			byte[] raw = DESEncryptUtil.doEncrypt(key, orgData);
			file = new File(file.getParent() + "\\en_" + file.getName());
		    
			FileOutputStream out = new FileOutputStream(file);
			out.write(raw);
			out.close();
			System.out.println("成功加密，加密文件位于:"+file.getAbsolutePath());
		} else if (args.length == 3 && args[0].equals("decrypt")) {
			
			File file = new File(args[1]);
			FileInputStream fis = new FileInputStream(file);			
			Key key = getKey(new FileInputStream(args[2]));
			InputStream raw = DESEncryptUtil.doDecrypt(key, fis);

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] tmpbuf = new byte[1024];
			int count = 0;
			while ((count = raw.read(tmpbuf)) != -1) {
				bout.write(tmpbuf, 0, count);
				tmpbuf = new byte[1024];
			}
			raw.close();
			byte[] orgData = bout.toByteArray();
			file = new File(file.getParent() + "\\rs_" + file.getName());
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(orgData);
			System.out.println("成功解密，解密文件位于:"+file.getAbsolutePath());
		}
	}
}
