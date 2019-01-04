package com.knowwhere.notshazamserver.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains certain String utility methods
 */
public class StringUtils{
    /**
     * This method returns SHA256 of the input string
     * @param data:String -> The string to be SHA'd
     * @return String
     */
    public static String getSHA256(String data){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte hash[] = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return new String(hash);
        }catch (NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
            return null;
        }
    }

    public static String toB64(String data){
        return new String(Base64.encodeBase64(data.getBytes()));
    }

}
