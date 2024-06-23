package org.pi.server.utils;

import com.aliyun.dm20151123.Client;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.config.AliyunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author hu1hu
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AliEmailUtils {
    private final AliyunConfig aliyunConfig;

    /**
     * 发送邮件
     * @param text 邮件内容
     * @param toAddress 收件人
     * @throws Exception 异常
     */
    public void send(String text, String toAddress) throws Exception {
        log.info("发送邮件，toAddress：{}",toAddress);
        // 创建Client实例并初始化
        Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(aliyunConfig.getAccessKeyId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(aliyunConfig.getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dm
        config.endpoint = aliyunConfig.getEmail().getEndpoint();
        Client client = new Client(config);

        SingleSendMailRequest singleSendMailRequest = new SingleSendMailRequest()
                .setAccountName(aliyunConfig.getEmail().getAccountName())
                .setAddressType(1)
                .setReplyToAddress(false)
                .setToAddress(toAddress)
                .setSubject(aliyunConfig.getEmail().getSubject())
                .setFromAlias(aliyunConfig.getEmail().getFromAlias())
                .setHtmlBody(
                        text
                );
        RuntimeOptions runtime = new RuntimeOptions();
        // 发送邮件
        client.singleSendMailWithOptions(singleSendMailRequest, runtime);
    }

    /**
     * 构建邮件内容
     * @param template 模板名称
     * @param map 模板中的占位符
     * @return 邮件内容
     */
    public String buildContent(String template, Map<String, String> map){
        //加载邮件html模板
        Resource resource = new ClassPathResource("templates/" + template + ".html");
        BufferedReader fileReader = null;
        StringBuffer content = new StringBuffer();
        String line;
        try {
            fileReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            while ((line = fileReader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}",e.getMessage());
        } finally { // 关闭流
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    log.warn("关闭流失败");
                }
            }
        }
        // 替换模板中的占位符
        map.forEach(
                (key, value) -> {
                    key = "${" + key + "}";
                    content.replace(content.indexOf(key),content.indexOf(key) + key.length(), value);
                }
        );
        return content.toString();
    }

}
