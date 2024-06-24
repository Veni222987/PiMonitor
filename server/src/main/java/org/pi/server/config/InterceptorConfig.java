package org.pi.server.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.pi.server.aop.GetAttributeResolver;
import org.pi.server.aop.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author huhuayu
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InterceptorConfig implements WebMvcConfigurer {
    private final CommonInterceptor commonInterceptor;

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns(
                        "/v1/**"
                ).excludePathPatterns(
                        // 发送验证码
                        "/v1/common/aliyun/code",
                        // 验证验证码
                        "/v1/common/authCode/loginOrRegister",
                        // 登录
                        "/v1/users/login",
                        // 获取第三方登录列表
                        "/v1/oauth/list",
                        // 第三方登录
                        "/v1/oauth/login/{type}",
                        // 第三方登录回调
                        "/v1/oauth/{type}/callback"
                );
    }

    /**
     * 添加自定义参数解析器
     * @param resolvers 参数解析器列表
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new GetAttributeResolver());
    }
}
