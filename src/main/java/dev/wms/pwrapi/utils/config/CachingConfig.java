package dev.wms.pwrapi.utils.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CachingConfig implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(List.of("pwr-news", "pwr-events", "forum-metadata", "reviews", "teachers", "usos-login"));
    }

    @CacheEvict(allEntries = true, cacheNames = "pwr-news")
    @Scheduled(fixedDelayString = "${pwr-api.cacheTTL.news}")
    public void reportCacheEvict(){
        log.info("evicting pwr-news from cache");

    }

    @CacheEvict(allEntries = true, cacheNames = "pwr-events")
    @Scheduled(fixedDelayString = "${pwr-api.cacheTTL.events}")
    public void reportCacheEventEvict(){
        log.debug("evicting pwr-events from cache");
    }

    @CacheEvict(allEntries = true, cacheNames = "usos-login")
    @Scheduled(fixedDelayString = "${usos-login.cacheTTL}")
    public void reportUsosLoginEvict(){
    }

    @CacheEvict(allEntries = true, cacheNames = {"forum-metadata", "reviews", "teachers"})
    @Scheduled(fixedDelayString = "${forum.cacheTTL}")
    public void evictForumData(){
        log.debug("evicting forum metadata from cache");
    }
    
}
