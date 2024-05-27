package org.pi.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.pi.server.model.entity.Host;
import org.pi.server.model.entity.User;

public interface UserService extends IService<User> {
    long login(String account, String password);
    long insertUser(User user);
    // 是否存在
    boolean exists(String type, String value);
    long getIDByPhoneNumber(String phoneNumber);
    long getIDByEmail(String email);
    boolean setPasswordByPhoneNumber(String phoneNumber, String password);
    boolean setPasswordByEmail(String email, String password);
    boolean modify(long userID, User user);
}
