package com.sta.xuptsta.util;

import com.sta.xuptsta.constant.JWTConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JWTUtil {

    public static String createToken(Long userId, long time) {
        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, JWTConstant.JWT_SECRET)
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setAudience(userId + "")
                .compact();
        return jwt;
    }

    public static Long getUserId(String token) {
        Long userId = Long.parseLong(Jwts.parser().setSigningKey(JWTConstant.JWT_SECRET).parseClaimsJws(token).getBody().getAudience());
        return userId;
    }

    public static boolean checkToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWTConstant.JWT_SECRET).parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
