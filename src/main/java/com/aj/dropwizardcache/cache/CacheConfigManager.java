package com.aj.dropwizardcache.cache;

import com.aj.dropwizardcache.domain.Student;
import com.aj.dropwizardcache.service.StudentService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfigManager.class);

    private static CacheConfigManager cacheConfigManager = new CacheConfigManager();

    public static CacheConfigManager getInstance() {
        return cacheConfigManager;
    }

    private static LoadingCache<String, Student> studentCache;

    public void initStudentCache(StudentService studentService) {
        if (studentCache == null) {
            studentCache =
                    CacheBuilder.newBuilder()
                            .concurrencyLevel(10)
                            .maximumSize(200) // Maximum of 200 records can be cached
                            .expireAfterAccess(30, TimeUnit.MINUTES) // Cache will expire after 30 minutes
                            .recordStats()
                            .build(new CacheLoader<String, Student>() { // Build the CacheLoader

                                @Override
                                public Student load(String universityId) throws Exception {
                                    logger.info("Fetching Student Data from DB/ Cache Miss");
                                    return studentService.getFromDatabase(universityId);
                                }
                            });
        }
    }

    public Student getStudentDataFromCache(String key) {
        try {
            CacheStats cacheStats = studentCache.stats();
            logger.info("CacheStats = {} ", cacheStats);
            return studentCache.get(key);
        } catch (ExecutionException e) {
            logger.error("Error Retrieving Elements from the Student Cache"
                    + e.getMessage());
        }
        return null;
    }

}
