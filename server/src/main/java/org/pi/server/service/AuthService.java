package org.pi.server.service;

import me.zhyd.oauth.model.AuthCallback;
import org.pi.server.model.entity.Auth;

import java.util.List;


/**
 * @author hu1hu
 */
public interface AuthService {

    long login(String type, AuthCallback callback);
    boolean unbind(String userID, String type);

    List<Auth> getAuthsByUserID(long userID);
}
