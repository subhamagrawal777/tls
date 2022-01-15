package com.golu.tls.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.golu.tls.models.AppResponse;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Singleton
@Path("/v1/housekeeping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api("General Housekeeping Apis")
@Timed
@ExceptionMetered
@AllArgsConstructor(onConstructor_ = @Inject)
public class HousekeepingResource {

    @GET
    @Path("/test")
    @ApiOperation(value = "Test Api")
    public Response getConfig() {
        return Response.ok()
                .entity(AppResponse.<String>builder()
                        .data("Hello")
                        .success(true)
                        .build())
                .build();
    }
}
