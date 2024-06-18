package org.pi.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.pi.server.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author hu1hu
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisServiceImpl implements RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * set key value
     * @param key key
     * @param value value
     */
    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * set key value expire
     * @param key key
     * @param value value
     * @param expire expire(秒级)
     */
    @Override
    public void set(String key, String value, long expire) {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * get value by key
     * @param key key
     * @return value
     */
    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * set expire
     * @param key key
     * @param expire expire(秒级)
     * @return boolean 是否设置成功
     */
    @Override
    public boolean expire(String key, long expire) {
        return Boolean.TRUE.equals(stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS));
    }

    /**
     * remove by key
     * @param key key
     */
    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * increment
     * @param key key
     * @param delta 自增步长
     * @return long
     */
    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key,delta);
    }

}
