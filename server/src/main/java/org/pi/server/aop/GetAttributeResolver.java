package org.pi.server.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.pi.server.annotation.GetAttribute;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 将Request Head中的参数值注入到注解的参数中
 * 对应规则：GetAttribute注解的value值，如果为空，则取参数名 <---> Head 参数名
 * 对应注解{@link GetAttribute}
 * @author hu1hu
 */
public class GetAttributeResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断是否支持该注解
     * @param parameter 方法参数
     * @return true 支持，false 不支持
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(GetAttribute.class) != null;
    }

    /**
     * 解析注解
     * @param parameter 方法参数
     * @param mavContainer 模型视图容器
     * @param webRequest 请求
     * @param binderFactory 数据绑定工厂
     * @return 注解的值
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // 获取请求
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        // 获取注解
        GetAttribute annotation = parameter.getParameterAnnotation(GetAttribute.class);
        // 如果注解不为空
        if (annotation != null) {
            // 获取注解的值
            String attributeName = annotation.value();
            // 如果注解的值为空，则取参数名
            if (attributeName.isEmpty()) {
                attributeName = parameter.getParameterName();
            }
            return request.getAttribute(attributeName);
        }
        return null;
    }
}
