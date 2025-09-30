package me.lahirudilhara.webchat.listners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheRestoreOnStartup {
    private final RedisCacheManager cacheManager;
    private final RedisTemplate<Object, Object> redisTemplate;

    public CacheRestoreOnStartup(RedisCacheManager cacheManager, RedisTemplate<Object, Object> redisTemplate) {
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void clearAllCache(){
        log.info("Clearing all caches");
        assert redisTemplate.getConnectionFactory() != null;
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
