package org.pi.server.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author hu1hu
 */
public interface AliyunOssService {
    /**
     * 生成post签名
     * @param dir 上传文件的目录
     * @return 返回签名
     * @throws Exception 异常
     */
    JSONObject generatePostSignature(String dir);

    /**
     * 生成URL 签名
     * @param fileName 文件名
     * @return 返回签名
     */
    String generateSignedURL(String fileName);
}
