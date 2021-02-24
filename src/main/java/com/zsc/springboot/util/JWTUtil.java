package com.zsc.springboot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsc.springboot.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * JWTUtil
 *@Author：黄港团
 *@Since：2021/1/12 21:32
 */
@Component
public class JWTUtil {
    private static String secret;
    private static long expire;
    //private static String header;
    @Value("${zsc.jwt.secret}")
    public void setSecret(String secret){
        this.secret = secret;
    }
    @Value("${zsc.jwt.expire}")
    public void setExpire(long expire){
        this.expire = expire;
    }

    /**
     * 校验token是否正确
     */
    public static boolean verify(String token) throws IOException {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取token中的信息无需密钥也能获得，这里获取登录者的userId,即手机号
     */
    public static String getUserId(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        }catch (Exception e){
            return null;
        }
    }

    public static String getUserPassword(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("password").asString();
        }catch (Exception e){
            return null;
        }
    }

    /**
     *  生成签名
     */
    public static String sign(User user){
        try{
            //过期时间
            Date date = new Date(System.currentTimeMillis() + expire * 1000);
            //密钥及算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //设置头部信息
            Map<String,Object> header = new HashMap<>(2);
            header.put("typ","JWT");
            header.put("alg","HS256");
            //附带userId信息，生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("phone",user.getPhone())
                    .withClaim("password",user.getPassword())
                    .withExpiresAt(date)  //设置过期时间
                    .sign(algorithm);

        }catch (Exception e){
            return null;
        }
    }

    /**
     *  生成签名  参数是字符串
     */
    public static String sign(String userId,String password){
        try{
            //过期时间
            Date date = new Date(System.currentTimeMillis() + expire * 1000);
            //密钥及算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //设置头部信息
            Map<String,Object> header = new HashMap<>(2);
            header.put("typ","JWT");
            header.put("alg","HS256");
            //附带userId信息，生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId",userId)
                    .withClaim("password",password)
                    .withExpiresAt(date)  //设置过期时间
                    .sign(algorithm);

        }catch (Exception e){
            return null;
        }
    }
}
