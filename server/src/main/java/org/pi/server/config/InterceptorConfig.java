package org.pi.server.config;

import org.pi.server.annotation.GetAttributeResolver;
import org.pi.server.aop.AuthCodeInterceptor;
import org.pi.server.aop.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private CommonInterceptor commonInterceptor;
    @Autowired
    private AuthCodeInterceptor authCodeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns(
                        "/v1/**"
                ).excludePathPatterns(
                        "/v1/common/aliyun/code", // 发送验证码
                        "/v1/common/authCode",  // 验证验证码
                        "/v1/users/login", // 登录
                        "/v1/oauth/login/{type}", // 第三方登录
                        "/v1/oauth/{type}/callback" // 第三方登录回调
                );
        registry.addInterceptor(authCodeInterceptor).addPathPatterns("/v1/common/authCode");
    }

    /**
     * 添加自定义参数解析器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new GetAttributeResolver());
    }
}
