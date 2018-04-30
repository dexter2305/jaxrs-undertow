package org.viddu.poc;

import io.swagger.annotations.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Api(value = "/hello")
public class HelloResource {

    @GET
    @Path("/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "Says hello to the given user",
            notes = "Attribute 'username' is required.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success. Returns 'Hello, {username}'"),
            @ApiResponse(code = 400, message = "Failure. Error while returning text message.")
    })

    public String sayHello(@ApiParam(value = "username", required = true) @PathParam("username") String username) {
        if (null != username && username.length() > 0) {
            return "Hello, " + username;
        } else {
            return "Hello World";
        }
    }
}
