package org.pi.server.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.config.AliyunConfig;
import org.pi.server.service.AliyunOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;

/**
 * @author hu1hu
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AliyunOssServiceImpl implements AliyunOssService {
    private final OSS ossClient;
    private final AliyunConfig aliyunConfig;

    /**
     * 生成OSS上传文件签名
     * @param dir 上传文件路径
     * @return 签名信息
     */
    @Override
    public JSONObject generatePostSignature(String dir) {
        JSONObject response = new JSONObject();
        try {
            long expireEndTime = System.currentTimeMillis() + aliyunConfig.getOss().getExpireTime() * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            response.put("ossAccessKeyId", aliyunConfig.getAccessKeyId());
            response.put("policy", encodedPolicy);
            response.put("signature", postSignature);
            response.put("dir", dir);
            response.put("host", aliyunConfig.getOss().getHost());
        } catch (OSSException oe) {
            log.error("Caught an OSSException ", oe);
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException ", ce);
            throw new RuntimeException(ce);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return response;
    }

    /**
     * 生成OSS文件临时访问URL
     * @param fileName 文件路径
     * @return 签名URL
     */
    @Override
    public String generateSignedURL(String fileName) {
        URL signedUrl;
        try {
            // 指定生成的签名URL过期时间，单位为毫秒。默认过期时间为1小时
            Date expiration = new Date(new Date().getTime() + 3600000L);
            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(aliyunConfig.getOss().getBucketName(), fileName, HttpMethod.GET);
            // 设置过期时间。
            request.setExpiration(expiration);
            // 通过HTTP GET请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);
        } catch (OSSException oe) {
            log.error("Caught an OSSException ", oe);
            throw new RuntimeException(oe);
        } catch (ClientException ce) {
            log.error("Caught an ClientException ", ce);
            throw new RuntimeException(ce);
        }
        return signedUrl.toString();
    }
}
