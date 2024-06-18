package org.pi.server.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author hu1hu
 */
@Slf4j
@Component
public class AuthCodeInterceptor implements HandlerInterceptor {

    /**
     * 目标资源方法运行前运行
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 返回true:放行，返回false，不放行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 1.获取请求头中的令牌（Authorization）
        String jwt = request.getHeader(JwtUtils.tokenHeader);
        // 2.判断令牌是否存在。
        if(!StringUtils.hasLength(jwt)){
            // 令牌不存在也通过
            return true;
        }
        // 3.解析token
        return JwtUtils.parseJWT(jwt, request, response);
    }

    /**
     * 目标资源方法运行后运行
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @param modelAndView 模型视图
     * @throws Exception 异常
     */
    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 视图渲染完毕后运行，最后运行
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @param ex 异常
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
