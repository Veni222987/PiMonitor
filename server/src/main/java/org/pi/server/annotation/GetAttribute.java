package org.pi.server.annotation;
import org.pi.server.aop.GetAttributeResolver;
import java.lang.annotation.*;

/**
 * 自定义注解，用于标记获取请求参数
 * 使用 {@link GetAttributeResolver} 解析器
 * @author hu1hu
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetAttribute {
    String value() default "";
}
