package org.pi.server.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRequestFactory factory;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthMapper authMapper;

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
        auth.setType(type);
        long userID = -1;
        if (split.length == 1) { // 登录或注册
            User user = new User();
            if (type.equals("gitee") || type.equals("github")) {
                log.info(jsonObject.toString());
                if (!jsonObject.getString("code").equals("2000")) {
                    return -1;
                }
                user.setUsername(jsonObject.getJSONObject("data").getString("username"));
                user.setAvatar(jsonObject.getJSONObject("data").getString("avatar"));
                user.setPassword("pim123456");
                auth.setOpenId(jsonObject.getJSONObject("data").getString("uuid"));
                QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("open_id", auth.getOpenId());
                if(authMapper.exists(queryWrapper)) { // 注册过了
                    userID = authMapper.selectOne(queryWrapper).getUserId();
                } else { // 未注册
                    userID = userService.insertUser(user);
                    auth.setUserId(userID);
                    authMapper.insert(auth);
                }
            }
        } else { // 绑定
            userID = Long.parseLong(split[0]);
            QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userID).eq("type", type);
            if (authMapper.exists(queryWrapper)) { // 已绑定
                return -2;
            } else { // 未绑定
                if (type.equals("gitee") || type.equals("github")) {
                    auth.setOpenId(jsonObject.getJSONObject("data").getString("uuid"));
                    auth.setUserId(userID);
                    authMapper.insert(auth);
                }
            }
        }
        return userID;
    }

    @Override
    public boolean unbind(String userID, String type) {
        QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.parseLong(userID)).eq("type", type);
        int delete = authMapper.delete(queryWrapper);
        return delete > 0;
    }
}
