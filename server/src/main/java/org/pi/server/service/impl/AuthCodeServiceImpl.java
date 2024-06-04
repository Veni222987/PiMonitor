package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jetbrains.annotations.NotNull;
import org.pi.server.model.entity.User;
import org.pi.server.service.AuthCodeService;
import org.pi.server.service.RedisService;
import org.pi.server.service.UserService;
import org.pi.server.utils.CodeUtils;
import org.pi.server.utils.JwtUtils;
import org.pi.server.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthCodeServiceImpl implements AuthCodeService {
    
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    /**
     * 登录或注册验证码
     * @param map
     * @return String jwt：成功  “-1”： 验证码不存在  “-2”： 验证码错误  “-3”：系统错误
     */
    @Override
    @Transactional
    public String loginOrRegisterAuthCode(@NotNull Map<String, Object> map) {
        String account = (String) map.get("account");
        String code = redisService.get(account);

        if (code == null) { // 验证码不存在
            return "-1";
        } else if (code.equals(map.get("code"))) { // 验证码正确
            long userID;
            if (account.contains("@")) { // 邮箱
                if (!userService.exists("email", account)) { // 未注册
                    User user = new User();
                    user.setEmail(account);
                    user.setPassword(PasswordUtils.encryptPassword("pim123456"));
                    user.setUsername("pim_" + CodeUtils.generateVerifyCode(6));
                    userService.insertUser(user);
                }
                userID = userService.getIDByEmail(account);
            } else { // 手机号
                if (!userService.exists("phone_number", account)) { // 未注册
                    User user = new User();
                    user.setPhoneNumber(account);
                    user.setPassword(PasswordUtils.encryptPassword("pim123456"));
                    user.setUsername("pim_" + CodeUtils.generateVerifyCode(6));
                    userService.insertUser(user);
                }
                userID = userService.getIDByPhoneNumber(account);
            }
            // 认证成功 下发jwt
            Map<String, Object> claims = new HashMap<>();
            claims.put("userID", userID + "");
            return JwtUtils.generateJwt(claims);
        } else { // 验证码错误
            return "-2";
        }
    }

    /**
     * 重置密码验证码
     * @param map
     * @return String jwt：成功  “-1”： 验证码
     */
    @Override
    public String resetPasswordAuthCode(Map<String, Object> map) {
        String account = (String) map.get("account");
        String code = redisService.get(account);

        if (code == null) { // 验证码不存在
            return "-1";
        } else if (code.equals(map.get("code"))) { // 验证码正确
            long userID;
            Map<String, Object> claims = new HashMap<>();
            if (account.contains("@")) { // 邮箱
                claims.put("email", account);
            } else { // 手机号
                claims.put("phoneNumber", account);
            }
            return JwtUtils.generateJwt(claims, 60 * 30L); // 30分钟
        } else { // 验证码错误
            return "-2";
        }
    }

    /**
     * 绑定手机号或邮箱
     * @param userID
     * @param map
     * @return String jwt：成功  “-1”： 验证码不存在  “-2”： 验证码错误  “-3”：系统错误
     */
    @Override
    public String bind(String userID, Map<String, Object> map) {
        String account = (String) map.get("account");
        String code = redisService.get(account);

        if (code == null) { // 验证码不存在
            return "-1";
        } else if (code.equals(map.get("code"))) { // 验证码正确
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            if (account.contains("@")) { // 绑定邮箱
                updateWrapper.eq("id", userID).set("email", account);
            } else { // 绑定手机号
                updateWrapper.eq("id", userID).set("phone_number", account);
            }
            boolean update = userService.update(updateWrapper);
            if (!update) {
                return "-3";
            }
            // 认证成功 下发jwt
            Map<String, Object> claims = new HashMap<>();
            claims.put("userID", userID);
            return JwtUtils.generateJwt(claims);
        } else {
            return "-2";
        }
    }
}
