package org.pi.server.service;

import java.util.Map;

public interface AuthCodeService {
    String loginOrRegisterAuthCode(Map<String,Object> map);

    String resetPasswordAuthCode(Map<String,Object> map);
    String bind(String userID, Map<String, Object> requestMap);
}
