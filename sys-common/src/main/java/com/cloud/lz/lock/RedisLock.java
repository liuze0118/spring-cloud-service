package com.cloud.lz.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.SafeEncoder;

import java.util.UUID;

@Component
public class RedisLock {
    @Autowired
    private static RedisTemplate redisTemplate;

    private static String presentValue;
    private static RedisSerializer<String> stringSerializer = new StringRedisSerializer();

    private static boolean lock(String key,int timeout,long expire){
        boolean b = tryLock(key, expire);
        while (!b){
            b = tryLock(key,expire);
        }
        return true;
    }

    public static boolean tryLock(String key,long expire) {
        String requestId = UUID.randomUUID().toString();
        boolean execute =(Boolean)redisTemplate.execute((RedisCallback) redisConnection -> {
            Object obj = redisConnection.execute("set", stringSerializer.serialize(key), stringSerializer.serialize(requestId), SafeEncoder.encode("NX"), SafeEncoder.encode("EX"), Protocol.toByteArray(expire));
            return obj != null;
        });
        if(execute){
            presentValue = requestId;
        }
        return execute;
    }

    public void unlock(String key) {
        //String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //Object result = new Jedis().eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(key, presentValue);
        if(absent!=null && !absent){
            String value = (String) redisTemplate.opsForValue().get(key);
            if(presentValue != null && presentValue.equals(value)){
                redisTemplate.delete(key);
            }
        }
    }



}
