package com.originaltek.botgo.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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


    /**
     * 无错误
     */
    public static final int TOKEN_CORRECT = 0;
    /**
     * 密码过期
     */
    public static final int TOKEN_ERR_EXPIRED = 1;
    public static final String TOKEN_ERR_EXPIRED_MSG = "密码过期";


    public static final int TOKEN_ERR_LOGOUT = 2;
    public static final String TOKEN_ERR_LOGOUT_MSG = "用户已经注销";


    /**
     * 其他错误
     */
    public static final int TOKEN_ERR_OTHER = 3;
    public static final String TOKEN_ERR_OTHER_MSG = "其他错误";





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


    public static String  generateToken(String subject, int expirationSeconds) {
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







    public static Map<String,Object> parseToken(String token) {

        Map<String,Object> parseResult = new HashMap<String, Object>(4);
        int result = TOKEN_CORRECT;
        String msg = "";

        String subject = null;
        try {
            Claims claims = Jwts.parser()

                    .setSigningKey(publicKey)

                    .parseClaimsJws(token)
                    .getBody();
            subject = claims.getSubject();
            parseResult.put("subject" , subject);

        }catch(ExpiredJwtException eje){
            result = TOKEN_ERR_EXPIRED;
            msg = TOKEN_ERR_EXPIRED_MSG;
        }catch (Exception e) {
            e.printStackTrace();
            result = TOKEN_ERR_OTHER;
            msg = TOKEN_ERR_OTHER_MSG;
        }
        parseResult.put("result" , result);
        parseResult.put("msg" , msg);
        return parseResult;
    }

}
