package org.pi.server.controller;

import com.xkcoding.justauth.AuthRequestFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.service.AuthService;
import org.pi.server.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hu1hu
 */
@Slf4j
@RestController
@RequestMapping("/v1/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan({"com.xkcoding.justauth"}) // Add this line
public class AuthController {
    private final AuthRequestFactory factory;
    private final AuthService authService;

    /**
     * 获取支持第三方登录的类型 (本应用支持列表)
     * @return 支持的第三方登录类型
     */
    @GetMapping("/list")
    public Result<Object> list() {
        return ResultUtils.success(Map.of("list", factory.oauthList()));
    }

    /**
     * 第三方登录
     * @param type 第三方账号类型
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    @GetMapping("/login/{type}")
    public void login(@PathVariable String type, @NotNull HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        // 随机生成state，用于校验回调state
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 绑定第三方账号
     * @param userID 用户ID
     * @param type 第三方账号类型
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    @GetMapping("/bind/{type}")
    public void bind(@GetAttribute("userID") @NotNull String userID, @PathVariable String type, @NotNull HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        // 随机生成state，用于校验回调state
        String state = AuthStateUtils.createState();
        // 拼接userID用于绑定
        state = userID + ":" + state;
        response.sendRedirect(authRequest.authorize(state));
    }

    /**
     * 解绑第三方账号
     * @param userID 用户ID
     * @param type 第三方账号类型
     * @return ResultCode.SUCCESS 成功 ResultCode.PARAMS_ERROR 参数错误
     */
    @DeleteMapping("/unbind/{type}")
    public Result<Object> unbind(@GetAttribute("userID") @NotNull String userID, @PathVariable @NotNull String type) {
        boolean unbind = authService.unbind(userID, type);
        return unbind ? ResultUtils.success() : ResultUtils.error(ResultCode.PARAMS_ERROR);
    }

    /**
     * 登录注册绑定回调函数
     * @param type 第三方账号类型
     * @param callback 回调信息
     * @return ResultCode.SUCCESS 成功 ResultCode.PARAMS_ERROR 参数错误 ResultCode.REPEAT_OPERATION 重复操作
     */
    @RequestMapping("/{type}/callback")
    public Result<Object> callback(@PathVariable @NotNull String type,AuthCallback callback) {
        long id = authService.login(type, callback);
        if (id == -1) {
            // 参数错误
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        } else if (id == -2) {
            // 重复绑定
            return ResultUtils.error(ResultCode.REPEAT_OPERATION);
        }
        // 生成jwt
        Map<String, Object> claims = Map.of("userID", id + "");
        String jwt = JwtUtils.tokenHead + JwtUtils.generateJwt(claims);
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", jwt);
        return ResultUtils.success(map);
    }
}