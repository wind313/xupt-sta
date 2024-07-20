package com.sta.xuptsta.util;

import org.springframework.util.DigestUtils;

public class MD5Util {
    public static String md5(String password)
    {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
