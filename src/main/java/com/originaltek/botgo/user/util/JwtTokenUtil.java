package com.originaltek.botgo.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * create_time : 2018/8/27 10:12
 * author      : chen.zhangchao
 * todo        :
 */
public class JwtTokenUtil {


    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.jks");
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;


    public static final String CONST_PASSWORD = "123456";
    //public static final String CONST_PASSWORD = "lhc123";
    public static final String CONST_ALIAS = "jwt";


    static {
        try {
            //KeyStore keyStore = KeyStore.getInstance("JKS");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, CONST_PASSWORD.toCharArray());
            Key key = keyStore.getKey(CONST_ALIAS , CONST_PASSWORD.toCharArray());
            if(key instanceof PrivateKey){
                Certificate cert = keyStore.getCertificate(CONST_ALIAS);
                PublicKey publicKey1 = cert.getPublicKey();
                KeyPair keyPair = new KeyPair(publicKey1 , (PrivateKey) key);
                privateKey = keyPair.getPrivate();
                publicKey = keyPair.getPublic();
            }



            //privateKey = (PrivateKey) keyStore.getKey(CONST_ALIAS, CONST_PASSWORD.toCharArray());
            //publicKey = keyStore.getCertificate(CONST_ALIAS).getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String generateToken(String subject, int expirationSeconds) {
        Map<String , Object> headerMap = new HashMap<String , Object>();
        headerMap.put("typ" , "JWT");
        headerMap.put("alg" , "RS256");
        return Jwts.builder()
                .setClaims(null)
                //.setHeader(headerMap)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public static String parseToken(String token) {
        String subject = null;
        try {
            Claims claims = Jwts.parser()

                    .setSigningKey(publicKey)

                    .parseClaimsJws(token)
                    .getBody();
            subject = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subject;
    }

}
