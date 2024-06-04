package org.pi.server.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class AuthCodeInterceptor implements HandlerInterceptor {

    /**
     * 目标资源方法运行前运行
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 返回true:放行，返回false，不放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求的url
        String url = request.getRequestURL().toString();

        // 2.获取请求头中的令牌（Authorization）
        String jwt = request.getHeader(JwtUtils.tokenHeader);

        // 3.判断令牌是否存在。
        if(!StringUtils.hasLength(jwt)){
            // 令牌不存在也通过
            return true;
        }

        // 4.解析token,如果解析失败，返回结果
        return JwtUtils.parseJWT(jwt, request, response);
    }

    @Override // 目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override // 视图渲染完毕后运行，最后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
