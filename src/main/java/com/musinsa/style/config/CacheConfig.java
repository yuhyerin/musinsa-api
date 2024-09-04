package com.musinsa.style.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "categoryMinPrices"
                ,"brandMinTotalPrice"
                ,"categoryMinMaxPrice"
                ,"allCategories"
        );
        cacheManager.setCaffeine(defaultCaffeineCacheBuilder());
        cacheManager.registerCustomCache("allCategories", customCaffeineCacheBuilder(1, TimeUnit.HOURS, 100).build());
        return cacheManager;
    }

    Caffeine<Object, Object> defaultCaffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100);
    }

    /**
     * 특정 캐시 설정을 생성하는 메서드
     *
     * @param duration 캐시 만료 시간
     * @param timeUnit 캐시 만료 시간 단위
     * @param maxSize  캐시 최대 크기
     * @return 설정된 Caffeine 객체
     */
    Caffeine<Object, Object> customCaffeineCacheBuilder(long duration, TimeUnit timeUnit, long maxSize) {
        return Caffeine.newBuilder()
                .expireAfterWrite(duration, timeUnit)
                .maximumSize(100);
    }

}
