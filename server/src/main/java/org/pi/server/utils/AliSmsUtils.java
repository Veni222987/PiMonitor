package org.pi.server.utils;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.config.AliyunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import com.aliyun.dysmsapi20170525.Client;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AliSmsUtils {
    private final AliyunConfig aliyunConfig;

    public void send(String code,String toAddress) throws Exception{
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliyunConfig.getAccessKeyId())
                .setAccessKeySecret(aliyunConfig.getAccessKeySecret());
        config.endpoint = aliyunConfig.getSms().getEndpoint();
        Client client = new Client(config);
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers(toAddress)
                .setSignName(aliyunConfig.getSms().getSignName())
                .setTemplateCode(aliyunConfig.getSms().getTemplateCode())
                .setTemplateParam("{\"code\":\""+code+"\"}");
        SendSmsResponse sendResponse = client.sendSms(sendSmsRequest);
    }
}
