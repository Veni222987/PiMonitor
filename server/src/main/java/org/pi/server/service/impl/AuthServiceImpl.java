package org.pi.server.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import org.apache.http.auth.AUTH;
import org.pi.server.mapper.AuthMapper;
import org.pi.server.model.entity.Auth;
import org.pi.server.model.entity.User;
import org.pi.server.service.AuthService;
import org.pi.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRequestFactory factory;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthMapper authMapper;

    @Override
    @Transactional
    public long login(String type, AuthCallback callback) {
        AuthRequest authRequest = factory.get(type);
        AuthResponse response = authRequest.login(callback);
        JSONObject jsonObject = JSONObject.parseObject(JSONUtil.toJsonStr(response));
        User user = new User();
        Auth auth = new Auth();
        auth.setType(type);
        long userID = -1;
        if (type.equals("gitee") || type.equals("github")) {
            if (jsonObject.getString("code") != "2000") {
                return -1;
            }
            user.setUsername(jsonObject.getJSONObject("data").getString("username"));
            user.setAvatar(jsonObject.getJSONObject("data").getString("avatar"));
            user.setPassword("pim123456");
            auth.setOpenId(jsonObject.getJSONObject("data").getString("uuid"));

            QueryWrapper<Auth> queryWrapper = new QueryWrapper<Auth>();
            queryWrapper.eq("open_id", auth.getOpenId());
            if(authMapper.exists(queryWrapper)) {
                userID = authMapper.selectOne(queryWrapper).getUserId();
            } else {
                userID = userService.insertUser(user);
                auth.setUserId(userID);
                authMapper.insert(auth);
            }
        }
        return userID;
    }
}
