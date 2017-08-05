package com.aj.dropwizardcache.resource;

import com.aj.dropwizardcache.DropwizardCacheConfiguration;
import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropwizardCacheHealthCheckResource extends HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(DropwizardCacheHealthCheckResource.class);

    private static String appName;

    public DropwizardCacheHealthCheckResource(DropwizardCacheConfiguration dropwizardCacheConfiguration){
       this.appName = dropwizardCacheConfiguration.getAppName();
    }

    @Override
    protected Result check() throws Exception {
        logger.info("App Name is: {}", appName);
        if("DropwizardCache".equalsIgnoreCase(appName)) {
            return Result.healthy();
        }
        return Result.unhealthy("Basic Dropwizard Service is down");
    }
}