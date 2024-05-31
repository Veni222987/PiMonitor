package org.pi.server.controller;

import com.xkcoding.justauth.AuthRequestFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.jetbrains.annotations.NotNull;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.service.AuthService;
import org.pi.server.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan({"com.xkcoding.justauth"})
public class AuthController {
    private final AuthRequestFactory factory;
    private final AuthService authService;

    @GetMapping
    public List<String> list() {
        return factory.oauthList();
    }

    @GetMapping("/login/{type}")
    public void login(@PathVariable String type, @NotNull HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(type);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }


    @RequestMapping("/{type}/callback")
    public Result<Object> login(@PathVariable String type, AuthCallback callback) {
        long id = authService.login(type, callback);
        if (id == -1) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        // jwt
        Map<String, Object> claims = Map.of("userID", id);
        String jwt = JwtUtils.tokenHead + JwtUtils.generateJwt(claims);
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", jwt);
        return ResultUtils.success(map);
    }

}