package org.pi.server.service;

public interface AliyunOssService {
    /**
     * 生成post签名
     * @param dir
     * @return
     * @throws Exception
     */
    String generatePostSignature(String dir);

    /**
     * 生成URL
     * @param fileName
     * @return
     */
    String generateSignedURL(String fileName);
}
