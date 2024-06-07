package org.pi.server.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import org.pi.server.mapper.AuthMapper;
import org.pi.server.model.entity.Auth;
import org.pi.server.model.entity.User;
import org.pi.server.service.AuthService;
import org.pi.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hu1hu
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthServiceImpl implements AuthService {
    private final AuthRequestFactory factory;
    private final UserService userService;
    private final AuthMapper authMapper;

    /**
     * 登录
     * @param type 第三方登录类型
     * @param callback 回调地址
     * @return 用户ID 登录成功 "-1" 参数错误（非法调用、过期） "-2" 已经绑定
     */
    @Override
    @Transactional
    public long login(String type, AuthCallback callback) {
        AuthRequest authRequest = factory.get(type);
        // 获取第三方登录信息
        AuthResponse response = authRequest.login(callback);
        JSONObject jsonObject = JSONObject.parseObject(JSONUtil.toJsonStr(response));
        String state = callback.getState();
        String[] split = state.split(":");

        Auth auth = new Auth();
        User user = new User();
        auth.setType(type);
        long userID;
        // Auth查询条件
        QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();

        if ("gitee".equals(type) || "github".equals(type)) {
            if (!"2000".equals(jsonObject.getString("code"))) {
                return -1;
            }
            String name = jsonObject.getJSONObject("data").getString("name");
            String avatar = jsonObject.getJSONObject("data").getString("avatar");
            // 用户信息
            user.setUsername(name);
            user.setAvatar(avatar);
            user.setPassword("pim123456");
            // Auth信息
            auth.setOpenId(jsonObject.getJSONObject("data").getString("uuid"));
            auth.setName(name);
            auth.setAvatar(avatar);
            auth.setBindTime(LocalDateTime.now());
            // 查询条件
            queryWrapper.eq("open_id", auth.getOpenId());
        } else {
            return -1;
        }

        if (split.length == 1) {
            // 登录或注册
            if(authMapper.exists(queryWrapper)) {
                // 注册过了
                userID = authMapper.selectOne(queryWrapper).getUserId();
            } else { // 未注册
                userID = userService.insertUser(user);
                auth.setUserId(userID);
                authMapper.insert(auth);
            }
        } else { // 绑定
            userID = Long.parseLong(split[0]);
            QueryWrapper<Auth> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("user_id", userID).eq("type", type);
            if (authMapper.exists(queryWrapper1)) {
                // 已绑定
                return -2;
            } else {
                // 未绑定
                auth.setUserId(userID);
                authMapper.insert(auth);
            }
        }
        return userID;
    }

    /**
     * 解绑
     * @param userID 用户ID
     * @param type 第三方登录类型 gitee github
     * @return 是否解绑成功
     */
    @Override
    public boolean unbind(String userID, String type) {
        QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.parseLong(userID)).eq("type", type);
        int delete = authMapper.delete(queryWrapper);
        return delete > 0;
    }

    /**
     * 根据用户ID获取第三方登录信息
     * @param userID 用户ID
     * @return 第三方登录信息
     */
    @Override
    public List<Auth> getAuthsByUserID(long userID) {
        QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userID);
        List<Auth> auths = authMapper.selectList(queryWrapper);
        return auths;
    }


}
