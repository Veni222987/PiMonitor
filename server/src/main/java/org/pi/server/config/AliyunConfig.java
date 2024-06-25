package org.pi.server.config;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.aliyun.dysmsapi20170525.Client;

/**
 * @author hu1hu
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun")
public class AliyunConfig {

    private String accessKeyId;
    private String accessKeySecret;
    private OssConfig oss;
    private EmailConfig email;
    private SmsConfig sms;

    @Data
    public static class OssConfig {
        private String endpoint;
        private String bucketName;
        private String host;
        private long expireTime = 3600; // 签名过期时间
        public String getHost() {
            return "https://" + bucketName + "." + endpoint;
        }
    }

    @Data
    public static class EmailConfig {
        private String endpoint;
        private String accountName;
        private String fromAlias;
        private String subject;
    }

    @Data
    public static class SmsConfig {
        private String endpoint;
        private String signName;
        private String templateCode;
    }

    /**
     * @return  {@link OSS}
     */
    @Bean
    public OSS createOssClient() {
        return new OSSClientBuilder().build(oss.getEndpoint(), accessKeyId, accessKeySecret);
    }

    /**
     * @return  {@link com.aliyun.dysmsapi20170525.Client}
     * @throws Exception 异常
     */
    @Bean
    public Client createSmsClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = sms.endpoint;
        return new Client(config);
    }
}
