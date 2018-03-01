package org.rta.rules.utils;

/**
 * this class having String related method.
 *
 */
public class StringsUtil {

    public static StringBuilder appendIfNotNull(StringBuilder sb, String strToAppend) {
        if (sb == null) {
            return null;
        }
        if (strToAppend != null) {
            sb.append(", ").append(strToAppend);
        }
        return sb;
    }
    
    public static boolean isNullOrEmpty(String str) {
        return (str == null) || str.trim().equals("");
    }
}
