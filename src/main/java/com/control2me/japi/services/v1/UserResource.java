package com.control2me.japi.services.v1;


import io.swagger.annotations.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(value = "/v1/users")
@Api("Manage Users")
public class UserResource {


    @GET
    @Path("/help")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(){
        return "Hello. Accessed over JAX RS";
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find an user by user id. User id is required.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success. Returns the user retrieved by id."),
            @ApiResponse(code = 400, message = "Failure. User does not exist. ")
    })
    public Response getUser(@PathParam("id") @ApiParam(required = true) String id){

        if (null == id || id.trim().length() == 0){
            return  Response.status(Response.Status.BAD_REQUEST).entity("Invalid user id " + id).build();
        } else{
            User user = new User();
            user.setId("1234");
            user.setName("Jon Snow");
            Response response = Response.ok(user).build();
            return response;
        }
    }


}
