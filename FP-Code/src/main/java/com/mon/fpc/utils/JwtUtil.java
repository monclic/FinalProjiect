package com.mon.fpc.utils;


import com.mon.fpc.Service.UserService;
import com.mon.fpc.core.enums.RedisKeyEnum;
import com.mon.fpc.core.exception.BaseException;
import com.mon.fpc.utils.redis.RedisUtil;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    @Resource
    private UserService userService;

    /**
     * 过期时间7天
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    /**
     * 加密密钥
     */
    private static final String KEY = "monclic";

    /**
     * 生成token
     */
    public  String createToken(String id, String pwd, String email) {
        if (email!=null) {
            return createToken(id, email);
        }else if (pwd!=null){
            return createToken(id, pwd);
        }
        throw new BaseException("create token err");
    }

    public String createToken(String id, String element) {
        Map<String, Object> header = new HashMap();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //setID:用户ID
        //setExpiration:token过期时间  当前时间+有效时间
        //setSubject:用户名
        //setIssuedAt:token创建时间
        //signWith:加密方式
        JwtBuilder builder = Jwts.builder().setHeader(header)
                .setId(id)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setSubject(element)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, KEY);
//        if (isToAdmin) {
//            builder.claim("isToAdmin", "is_to_admin");
//        }
        return builder.compact();
    }

    /**
     * 验证token是否有效
     *
     * @param token 请求头中携带的token
     * @return token验证结果  2-token过期；1-token认证通过；0-token认证失败
     */
    public int verify(String token) {
        Claims claims = null;
        try {
            //token过期后，会抛出ExpiredJwtException 异常，通过这个来判定token过期，
            claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return 2;
        }

        //从token中获取用户id，查询该Id的用户是否存在，存在则token验证通过
        String id = claims.getId();

        //验证token是否在redis中,若存在isPresent()返回true
        Optional<Object> optional = RedisUtil.get(RedisKeyEnum.USER_TOKEN_INDEX, id);
        if (optional.isPresent()) {
            if (!token.equals(optional.get().toString())) {
                return 8;
            }
        } else {
            return 9;
        }

        com.entity.User user;
        Optional<Object> o = RedisUtil.get(RedisKeyEnum.USER_INFO, id);
        if (!o.isPresent()) {
            user = userService.getById(id);//这里可以放redis做验证。
            RedisUtil.set(RedisKeyEnum.USER_INFO, user.getUserId().toString(), user, 5, TimeUnit.MINUTES);
        } else {
            user = (com.entity.User) o.get();
        }
        if (user != null) {
            RedisUtil.setEachTimes(RedisKeyEnum.USER_LOGIN_LAST_TIME, user.getUserId().toString(), TimeUtils.getLocalDateTimeNow(), 24, TimeUnit.HOURS);
            LoginUserHolder.set(user);
            return 1;
        } else {
            return 0;
        }
    }

    public Claims getClaims( String token ){
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
}

