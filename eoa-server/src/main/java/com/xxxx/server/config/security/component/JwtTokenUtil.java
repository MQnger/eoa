package com.xxxx.server.config.security.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: MAQJ
 * @Date: 2021/05/21/9:35
 * @Description: JWT Token 工具类
 */
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成token
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        // 根据用户信息生成tomken
        return generateToken(claims);
    }

    /**
     * 从token中获取登录用户名
     *
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFormToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            e.printStackTrace();
        }
        return username;
    }

    /**
     * 验证token是否有效
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String userNameFromToken = getUserNameFromToken(token);
        // 有效返回true，boolean默认返回fasle
        return userNameFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否可以被刷新
     *
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        // 返回true，所以要取反
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     *  获取token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFormToken(token);
        // 刷新时间
        claims.put(CLAIM_KEY_CREATED,new Date());
        // 重新成才token
        return generateToken(claims);
    }


    /**
     * 判断token是否失效
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expiredDateFromToken = getExpiredDateFromToken(token);
        // 返回值为true，即token没过期
        return expiredDateFromToken.before(new Date());
    }


    /**
     * 从token获取过期时间
     *
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据荷载生成jwt token
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        // 创建jwt并赋值
        return Jwts.builder().setClaims(claims)
                // 设置过期时间，目前时间+设置的过期时间组成
                .setExpiration(generateExpirationDate())
                // 设置签名方式和盐
                .signWith(SignatureAlgorithm.HS512, secret)
                // Builds the JWT and serializes it to a compact, URL-safe string
                // 构建JWT并将其序列化为一个紧凑的、url安全的字符串
                .compact();
    }

    /**
     * 从token中获得JWT的荷载
     *
     * @param token
     * @return
     */
    private Claims getClaimsFormToken(String token) {
        Claims claims = null;
        try {
            // 返回一个JwtParser，用于解析JWT，配置上解析的盐
            claims = Jwts.parser().setSigningKey(secret)
                    // 要解析的对象
                    .parseClaimsJws(token)
                    // 获得JWT主题，一个String或者claims
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }


    /**
     * 生成token过期时间
     *
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    ArrayList a = new ArrayList();


    private void generateExpirationDate1() {
        a.size();
    }

}
