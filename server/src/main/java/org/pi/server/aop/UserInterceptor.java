package org.pi.server.aop;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {
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
        System.out.println(url);
        // 2.获取请求头中的令牌（Authorization）
        String jwt = request.getHeader("Authorization");

        // 3.判断令牌是否存在。如果不存在，返回错误结果
        if(!StringUtils.hasLength(jwt)){
            Result<Object> error = ResultUtils.error(ResultCode.NO_AUTH_ERROR);
            // 手动转换 对象--json ------> 阿里巴巴fastJSON
            String result = JSONObject.toJSONString(error);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(result);
            return false;
        }

        // 4.解析token,如果解析失败，返回结果
        try{
            Claims claims = JwtUtils.parseJWT(jwt);
            claims.forEach(request::setAttribute);
        }catch(Exception e){
            Result<Object> error = ResultUtils.error(ResultCode.TOKEN_EXPIRED);
            // 手动转换 对象--json ------> 阿里巴巴fastJSON
            String result = JSONObject.toJSONString(error);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(result);
            return false;
        }
        // 5.放行
        return true;
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
