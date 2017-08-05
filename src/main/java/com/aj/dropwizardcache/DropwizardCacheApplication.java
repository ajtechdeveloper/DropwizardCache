package com.aj.dropwizardcache;

import com.aj.dropwizardcache.resource.CacheResource;
import com.aj.dropwizardcache.resource.DropwizardCacheHealthCheckResource;
import com.aj.dropwizardcache.resource.PingResource;
import com.aj.dropwizardcache.cache.CacheConfigManager;
import com.aj.dropwizardcache.service.StudentService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DropwizardCacheApplication extends Application<DropwizardCacheConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(DropwizardCacheApplication.class);

	public static void main(String[] args) throws Exception {
		new DropwizardCacheApplication().run("server", args[0]);
	}

    @Override
    public void initialize(Bootstrap<DropwizardCacheConfiguration> b) {
    }

	@Override
	public void run(DropwizardCacheConfiguration config, Environment env)
			throws Exception {
        CacheConfigManager cacheConfigManager = CacheConfigManager
                .getInstance();
	    StudentService studentService = new StudentService();
        cacheConfigManager.initStudentCache(studentService);
	    logger.info("Registering RESTful API resources");
		env.jersey().register(new PingResource());
        env.jersey().register(new CacheResource());
		env.healthChecks().register("DropwizardCacheHealthCheck",
				new DropwizardCacheHealthCheckResource(config));
	}
}
