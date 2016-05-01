package com.ap.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {

    private static final transient String OS = System.getProperty("os.name").toLowerCase();

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz);
    }

    private static Logger getLogger() {
        return Logger.getLogger(CommonUtils.class);
    }

    public static String digestPackString(byte[] bytes, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance(algorithm);
        md.update(bytes);
        byte[] digest = md.digest();
        return new String(Base64.encodeBase64(digest));
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }

    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }

}
