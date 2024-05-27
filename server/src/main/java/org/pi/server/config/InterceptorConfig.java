package org.pi.server.config;

import org.pi.server.annotation.GetAttributeResolver;
import org.pi.server.aop.AuthCodeInterceptor;
import org.pi.server.aop.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private UserInterceptor userInterceptor;
    @Autowired
    private AuthCodeInterceptor authCodeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns(
                        "/v1/users/register",
                        "/v1/users/resetPassword",
                        "/v1/users/modify"
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
