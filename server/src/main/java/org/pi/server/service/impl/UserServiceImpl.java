package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.pi.server.mapper.UserMapper;
import org.pi.server.model.entity.User;
import org.pi.server.service.UserService;
import org.pi.server.utils.CodeUtils;
import org.pi.server.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param account
     * @param password
     * @return -1 账号不存在 -2 密码错误 id 登录成功
     */
    @Override
    public long login(String account, String password) {
        password = PasswordUtils.encryptPassword(password);
        // 登录
        // account 是邮箱
        User user = null;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (account.contains("@")) {
            queryWrapper.eq("email", account);
        } else { // account 是手机号
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


    @Override
    public long insertUser(User user) {
        user.setId(null);
        user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));
        user.setUsername("pim" + CodeUtils.generateVerifyCode(10));
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public boolean exists(String type, String value) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(type, value);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public long getIDByPhoneNumber(String phoneNumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number", phoneNumber);
        queryWrapper.last("LIMIT 1");
        return userMapper.selectOne(queryWrapper).getId();
    }

    @Override
    public long getIDByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.last("LIMIT 1");
        return userMapper.selectOne(queryWrapper).getId();
    }

    @Override
    public boolean setPasswordByPhoneNumber(String phoneNumber, String password) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("phone_number", phoneNumber).set("password", PasswordUtils.encryptPassword(password));
        return update(updateWrapper);
    }

    @Override
    public boolean setPasswordByEmail(String email, String password) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", email).set("password", PasswordUtils.encryptPassword(password));
        return update(updateWrapper);
    }

    @Override
    public boolean modify(long userID, User user) {
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
}
