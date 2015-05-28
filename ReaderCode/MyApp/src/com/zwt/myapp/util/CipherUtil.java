package com.zwt.myapp.util;

import java.security.MessageDigest;

/**  
 * 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹灞戦敓鏂ゆ嫹鑺庨敓鏂ゆ嫹閿熻銈忔嫹閿熸枻鎷烽敓锟� 
 */  
public class CipherUtil{  
      
    //鍗侀敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熻纰夋嫹閿熻鍑ゆ嫹閿熸枻鎷锋槧閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�  
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",  
        "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};  
      
    /** * 閿熸枻鎷穒nputString閿熸枻鎷烽敓鏂ゆ嫹     */  
    public static String generatePassword(String inputString){  
        return encodeByMD5(inputString);  
    }  
      
      /** 
       * 閿熸枻鎷疯瘉閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋瑺閿熸枻鎷烽敓楗猴拷 
     * @param password    閿熸枻鎷烽敓鏉扮尨鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟� 
     * @param inputString    閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷峰潃閿熸枻鎷烽敓锟� 
     * @return    閿熸枻鎷疯瘉閿熸枻鎷烽敓鏂ゆ嫹閿熺祴RUE:閿熸枻鎷风‘ FALSE:閿熸枻鎷烽敓鏂ゆ嫹 
     */  
    public static boolean validatePassword(String password, String inputString){  
        if(password.equals(encodeByMD5(inputString))){  
            return true;  
        } else{  
            return false;  
        }  
    }  
    /**  閿熸枻鎷烽敓琛楀嚖鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹MD5閿熸枻鎷烽敓鏂ゆ嫹     */  
    private static String encodeByMD5(String originString){  
        if (originString != null){  
            try{  
                //閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鎸囬敓鏂ゆ嫹閿熷娉曢敓鏂ゆ嫹閿熺嫛纰夋嫹閿熸枻鎷锋伅鎽樿  
                MessageDigest md = MessageDigest.getInstance("MD5");  
                //浣块敓鏂ゆ嫹鎸囬敓鏂ゆ嫹閿熸枻鎷烽敓琛楁枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓绉割�鳖亷鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋嫝閿熼ズ浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹鎽樿閿熸枻鎷烽敓鏂ゆ嫹  
                byte[] results = md.digest(originString.getBytes());  
                //閿熸枻鎷烽敓鐭鎷烽敓鏂ゆ嫹閿熻鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓琛楀嚖鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹  
                String resultString = byteArrayToHexString(results);  
                return resultString.toUpperCase();  
            } catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }  
        return null;  
    }  
      
    /**  
     * 杞敓鏂ゆ嫹閿熻鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹涓哄崄閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓琛楀嚖鎷烽敓鏂ゆ嫹 
     * @param     閿熻鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 
     * @return    鍗侀敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熻鍑ゆ嫹閿熸枻鎷� 
     */  
    private static String byteArrayToHexString(byte[] b){  
        StringBuffer resultSb = new StringBuffer();  
        for (int i = 0; i < b.length; i++){  
            resultSb.append(byteToHexString(b[i]));  
        }  
        return resultSb.toString();  
    }  
      
    /** 閿熸枻鎷蜂竴閿熸枻鎷烽敓琛楁枻鎷疯浆閿熸枻鎷烽敓鏂ゆ嫹鍗侀敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷峰紡閿熸枻鎷烽敓琛楀嚖鎷烽敓鏂ゆ嫹     */  
    private static String byteToHexString(byte b){  
        int n = b;  
        if (n < 0)  
            n = 256 + n;  
        int d1 = n / 16;  
        int d2 = n % 16;  
        return hexDigits[d1] + hexDigits[d2];  
    }  
}  