package org.pi.server.aop;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author huhuayu
 */
@Slf4j
@Component
public class CommonInterceptor implements HandlerInterceptor {

    /**
     * 目标资源方法运行前运行(解析token，判断token是否过期，是否有效)
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return true:放行，false:不放行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 1.获取请求头中的令牌（Authorization）
        String jwt = request.getHeader("Authorization");
        // 2.判断令牌是否存在。如果不存在，返回错误结果
        if(!StringUtils.hasLength(jwt)){
            Result<Object> error = ResultUtils.error(ResultCode.NO_AUTH_ERROR);
            response.setStatus(error.getCode()/100);
            // 手动转换 对象--json ------> 阿里巴巴fastJSON
            String result = JSONObject.toJSONString(error);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(result);
            return false;
        }
        // 3.解析token
        return JwtUtils.parseJWT(jwt, request, response);
    }

    /**
     * 目标资源方法运行后运行
     * @param request HTTP请求
     * @param response HTTP响应
     * @param handler 处理器
     * @param modelAndView 模型和视图
     * @throws Exception 异常
     */
    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 视图渲染完毕后运行，最后运行
     * @param request HTTP请求
     * @param response HTTP响应
     * @param handler 处理器
     * @param ex 异常
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
