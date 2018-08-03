package com.mushiny.security;

import sun.misc.BASE64Encoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class TestEncryptAndDecrypt {
    static BASE64Encoder base64Encoder = new BASE64Encoder();
    public static void main(String[] args) throws Exception {
        String input = "黎庆剑";
        Cipher cipher = Cipher.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) PublicKeyReader.get("d:/publicKeyFile");
        RSAPrivateKey privKey = (RSAPrivateKey) PrivateKeyReader.get("d:/privateKeyFile");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        //加密后的东西
        System.out.println("cipher: " + base64Encoder.encode(cipherText));
        //开始解密
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] plainText = cipher.doFinal(cipherText);
        System.out.println("plain : " + new String(plainText));
    }

}
