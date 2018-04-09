package com.udacity.norbi930523.manutdapp.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "manutd",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.manutdapp.norbi930523.udacity.com",
                ownerName = "backend.manutdapp.norbi930523.udacity.com",
                packagePath = ""
        )
)
public class ManUtdEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public SimpleResponse sayHi(@Named("name") String name) {
        SimpleResponse response = new SimpleResponse();
        response.setData("Hi, " + name);

        return response;
    }

}
