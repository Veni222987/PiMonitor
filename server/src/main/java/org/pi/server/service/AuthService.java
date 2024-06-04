package org.pi.server.service;

import me.zhyd.oauth.model.AuthCallback;


public interface AuthService {

    long login(String type, AuthCallback callback);
    boolean unbind(String userID, String type);
}
