package com.control2me.japi;

import io.swagger.jaxrs.config.BeanConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class SwaggerDocumenSetup extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
        BeanConfig config = new BeanConfig();
        config.setVersion("1.0.0");
        config.setTitle("Demo of Swagger document");
        config.setDescription("Swagger document generation through io.swagger annotation api");

        config.setSchemes(new String[]{"http", "https"});
        //fixme: localhost will never work. Should be the real hosting server
        config.setHost("localhost:8080");
        config.setBasePath("/demo/services");
        config.setResourcePackage("com.control2me.japi.services");
        config.setScan(true);
    }
}
