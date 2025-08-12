package com.example.authentication.repositories;

import com.example.authentication.util.models.RecoveryCodeEntry;
import com.example.authentication.util.models.RedisKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final static TimeUnit LIFE_TIME_FORMAT = TimeUnit.SECONDS;

    public Optional<RecoveryCodeEntry> findRecoveryCodeEntryByTelegramId(long telegramId){
        Optional<RecoveryCodeEntry> optional =  Optional.ofNullable((RecoveryCodeEntry) redisTemplate.opsForValue().get(convertToKey(telegramId, RedisKey.RECOVERY_CODE)));
        return optional;
    }

    private String convertToKey(long telegramId, RedisKey key){
        return String.format(key.getPattern(), telegramId);
    }

    public void saveWithLifeTime(long telegramIdKey, RecoveryCodeEntry recoveryCodeEntry, long lifeTime){
        redisTemplate.opsForValue().set(convertToKey(telegramIdKey, RedisKey.RECOVERY_CODE), recoveryCodeEntry, lifeTime, LIFE_TIME_FORMAT);
    }

    public void removeRecoveryCodeEntry(long telegramId){
        redisTemplate.delete(convertToKey(telegramId, RedisKey.RECOVERY_CODE));
    }

    public void updateRecoveryCodeEntry(long telegramId, RecoveryCodeEntry entry){
        String key = convertToKey(telegramId, RedisKey.RECOVERY_CODE);
        long ttl = redisTemplate.getExpire(key, LIFE_TIME_FORMAT);
        if (ttl > 0){
            redisTemplate.opsForValue().set(key, entry, ttl, LIFE_TIME_FORMAT);
        }
    }
}
