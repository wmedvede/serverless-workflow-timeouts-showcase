package org.kie.kogito.examples;

import java.time.ZonedDateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.cloudevents.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class DataIndexMockResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexMockResource.class);

    @Path("/hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from DataIndexMock: " + ZonedDateTime.now();
    }

    @Path("/jobs/resource")
    @POST
    @Consumes("*/*")
    @Produces(MediaType.APPLICATION_JSON)
    public Response onCloudEvent(CloudEvent event) {
        LOGGER.debug("Event received: {}", event);
        return Response.accepted().build();
    }
}
