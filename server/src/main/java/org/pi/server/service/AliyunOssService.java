package org.pi.server.service;

import com.alibaba.fastjson.JSONObject;

public interface AliyunOssService {
    /**
     * 生成post签名
     * @param dir
     * @return
     * @throws Exception
     */
    JSONObject generatePostSignature(String dir);

    /**
     * 生成URL
     * @param fileName
     * @return
     */
    String generateSignedURL(String fileName);
}
