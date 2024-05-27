package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.pi.server.model.entity.User;
import org.pi.server.service.AuthCodeService;
import org.pi.server.service.RedisService;
import org.pi.server.service.UserService;
import org.pi.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthCodeServiceImpl implements AuthCodeService {
    
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    @Override
    public String loginAuthCode(Map<String, Object> map) {
        if (map.containsKey("phoneNumber")) {
            if (redisService.get(map.get("phoneNumber").toString()) == null) {
                return "-1";
            }
            if (redisService.get(map.get("phoneNumber").toString()).equals(map.get("code"))) {
                long userID = userService.getIDByPhoneNumber(map.get("phoneNumber").toString());
                // 认证成功 下发jwt
                Map<String, Object> claims = new HashMap<>();
                claims.put("userID", userID);
                return JwtUtil.generateJwt(claims, 30 * 60L); // 30分钟
            }
        } else if (map.containsKey("email")) {
            if (redisService.get(map.get("email").toString()) == null) {
                return "-1";
            }
            if (redisService.get(map.get("email").toString()).equals(map.get("code"))) {
                long userID = userService.getIDByEmail(map.get("email").toString());
                // 认证成功 下发jwt
                Map<String, Object> claims = new HashMap<>();
                claims.put("userID", userID);
                return JwtUtil.generateJwt(claims, 30 * 60L); // 30分钟
            }
        }
        return "-2";
    }

    @Override
    public String registerAuthCode(Map<String, Object> map) {
        if (map.containsKey("phoneNumber")) {
            if (redisService.get(map.get("phoneNumber").toString()) == null) {
                return "-1";
            }
            if (redisService.get(map.get("phoneNumber").toString()).equals(map.get("code"))) {
                // 认证成功 下发jwt
                Map<String, Object> claims = new HashMap<>();
                claims.put("phoneNumber", map.get("phoneNumber"));
                return JwtUtil.generateJwt(claims, 30 * 60L); // 30分钟
            }
        } else if (map.containsKey("email")) {
            if (redisService.get(map.get("email").toString()) == null) {
                return "-1";
            }
            if (redisService.get(map.get("email").toString()).equals(map.get("code"))) {
                // 认证成功 下发jwt
                Map<String, Object> claims = new HashMap<>();
                claims.put("email", map.get("email"));
                return JwtUtil.generateJwt(claims, 30 * 60L);
            }
        }
        return "-2";
    }

    @Override
    public String bind(String userID, Map<String, Object> map) {
        if (map.containsKey("phoneNumber")) {
            if (redisService.get(map.get("phoneNumber").toString()) == null) {
                return "-1";
            }
            if (redisService.get(map.get("phoneNumber").toString()).equals(map.get("code"))) {
                UpdateWrapper<User> updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", userID).set("phone_number", map.get("phoneNumber"));
                boolean update = userService.update(updateWrapper);
                if (!update) {
                    return "-3";
                }
                // 认证成功 下发jwt
                Map<String, Object> claims = new HashMap<>();
                claims.put("userID", userID);
                return JwtUtil.generateJwt(claims, 30 * 60L); // 30分钟
            }
        } else if (map.containsKey("email")) {
            if (redisService.get(map.get("email").toString()) == null) {
                return "-1";
            }
            if (redisService.get(map.get("email").toString()).equals(map.get("code"))) {
                UpdateWrapper<User> updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", userID).set("email", map.get("email"));
                boolean update = userService.update(updateWrapper);
                if (!update) {
                    return "-3";
                }
                // 认证成功 下发jwt
                Map<String, Object> claims = new HashMap<>();
                claims.put("userID", userID);
                return JwtUtil.generateJwt(claims, 30 * 60L);
            }
        }
        return "-2";
    }
}
