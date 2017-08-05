package com.aj.dropwizardcache.resource;

import com.aj.dropwizardcache.cache.CacheConfigManager;
import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/cache")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "cache", description = "Cache Resource for getting student data from DB/cache")
public class CacheResource {

    private static final Logger logger = LoggerFactory.getLogger(CacheResource.class);

    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "cache")
    public Response cache() {
        logger.info("In CacheResource.cache()...Get Student Data");
        //On the first call, data will be fetched from DB and
        //cache will be populated with the corresponding student record
        //On all subsequent calls, data will be returned from the cache
        for (int i = 1; i < 4; i++) {
            getStudentData(i);
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student Data has been retrieved");
        return Response.ok(response).build();
    }

    private void getStudentData(int i) {
        logger.info("********** Call " + String.valueOf(i) + " Started **********");
        logger.info("Call " + String.valueOf(i) + ": {}", CacheConfigManager.getInstance().getStudentDataFromCache("S100"));
        logger.info("Call " + String.valueOf(i) + ": {}", CacheConfigManager.getInstance().getStudentDataFromCache("M101"));
        logger.info("Call " + String.valueOf(i) + ": {}", CacheConfigManager.getInstance().getStudentDataFromCache("P102"));
        logger.info("********** Call " + String.valueOf(i) + " Ended **********");
    }
}
