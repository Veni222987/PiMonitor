package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.pi.server.mapper.UserMapper;
import org.pi.server.model.entity.User;
import org.pi.server.service.AliyunOssService;
import org.pi.server.service.UserService;
import org.pi.server.utils.CodeUtils;
import org.pi.server.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hu1hu
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final AliyunOssService aliyunOssService;

    /**
     * 登录
     * @param account - 手机号或邮箱
     * @param password - 密码
     * @return -1 账号不存在 -2 密码错误 id 登录成功
     */
    @Override
    public long login(String account, String password) {
        password = PasswordUtils.encryptPassword(password);
        // 登录
        // account 是邮箱
        User user;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (account.contains("@")) {
            // 邮箱登录
            queryWrapper.eq("email", account);
        } else if (account.startsWith("pim")) {
            // 账号登录 pim + id
            queryWrapper.eq("id", Long.parseLong(account.substring(3)));
        } else {
            // 手机号登录
            queryWrapper.eq("phone_number", account);
        }
        queryWrapper.last("LIMIT 1");
        user = userMapper.selectOne(queryWrapper);
        if ( user == null ) {
            return -1;
        } else if (!user.getPassword().equals(password)) {
            return -2;
        }
        return user.getId();
    }

    /**
     * 注册
     * @param user 用户信息
     * @return 用户ID
     */
    @Override
    public long insertUser(User user) {
        user.setId(null);
        user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));
        user.setUsername("pim" + CodeUtils.generateVerifyCode(10));
        userMapper.insert(user);
        return user.getId();
    }

    /**
     * 检查是否存在
     * @param type - phone_number or email
     * @param value - phone number or email
     * @return true 存在 false 不存在
     */
    @Override
    public boolean exists(String type, String value) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(type, value);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 通过手机号获取用户ID
     * @param phoneNumber 手机号
     * @return 用户ID
     */
    @Override
    public long getIDByPhoneNumber(String phoneNumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number", phoneNumber);
        queryWrapper.last("LIMIT 1");
        return userMapper.selectOne(queryWrapper).getId();
    }

    /**
     * 通过邮箱获取用户ID
     * @param email 邮箱
     * @return 用户ID
     */
    @Override
    public long getIDByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.last("LIMIT 1");
        return userMapper.selectOne(queryWrapper).getId();
    }

    /**
     * 通过手机号设置密码
     * @param phoneNumber 手机号
     * @param password 密码
     * @return true 成功 false 失败
     */
    @Override
    public boolean setPasswordByPhoneNumber(String phoneNumber, String password) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("phone_number", phoneNumber).set("password", PasswordUtils.encryptPassword(password));
        return update(updateWrapper);
    }

    /**
     * 通过邮箱设置密码
     * @param email 邮箱
     * @param password 密码
     * @return true 成功 false 失败
     */
    @Override
    public boolean setPasswordByEmail(String email, String password) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", email).set("password", PasswordUtils.encryptPassword(password));
        return update(updateWrapper);
    }

    /**
     * 修改用户信息
     * @param userID 用户ID
     * @param user 用户信息
     * @return true 成功 false 失败
     */
    @Override
    public boolean modify(long userID, @NotNull User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userID);
        System.out.println(userID);
        if (user.getUsername() != null) {
            updateWrapper.set("username", user.getUsername());
        }
        if(user.getAvatar() != null) {
            updateWrapper.set("avatar", user.getAvatar());
        }
        return update(updateWrapper);
    }

    /**
     * 通过用户ID获取用户信息
     * @param userID 用户ID
     * @return 用户信息
     */
    @Override
    public User getByID(long userID) {
        User user = getById(userID);
        if (user == null) {
            return null;
        } else {
            String avatar = user.getAvatar();
            if (avatar != null && !avatar.startsWith("http")) {
                user.setAvatar(aliyunOssService.generateSignedURL(avatar));
            }
        }
        return user;
    }

    /**
     * 邮箱、手机号解绑
     * @param userID 用户ID
     * @return 用户信息
     */
    @Override
    public boolean unbind(long userID, @NotNull String type) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userID);
        if (type.equals("email")) {
            updateWrapper.set("email", null);
        } else if (type.equals("phone_number")) {
            updateWrapper.set("phone_number", null);
        }
        return update(updateWrapper);
    }
}
