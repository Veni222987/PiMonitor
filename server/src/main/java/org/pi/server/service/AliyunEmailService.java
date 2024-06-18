package org.pi.server.service;

import java.util.Map;

public interface AliyunEmailService {
    void send(String template, String toAddress, Map<String, String> map) throws Exception;
}
