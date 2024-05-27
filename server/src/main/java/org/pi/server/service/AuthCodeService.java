package org.pi.server.service;

import java.util.Map;

public interface AuthCodeService {
    String loginAuthCode(Map<String,Object> map);
    String registerAuthCode(Map<String,Object> map);

    String bind(String userID, Map<String, Object> requestMap);
}
