package com.control2me.japi;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class JAXRSServerV2 {

    private static final Logger logger = LoggerFactory.getLogger(JAXRSServerV2.class);

    private Undertow server;

    public static void main(String[] args) {
        JAXRSServerV2 server;
        try {
            server = new JAXRSServerV2("demo", "localhost", 8080);
            server.start();
        } catch (ServletException e) {
            logger.error(e.getMessage(), e);
        } catch (Throwable t){
            logger.error(t.getMessage(), t);
        }

    }


    JAXRSServerV2(final String applicationName, final String host, final Integer port) throws ServletException {

        final String contextPath  = "/" + applicationName;
        HttpHandler jaxrsServletHandler = buildJAXRSServletHandler(contextPath);
        HttpHandler webHandler = buildWebHandler();

        final HttpHandler pathHandler = Handlers.path(Handlers.redirect("/api-docs"))
                .addExactPath("/", Handlers.redirect("/api-docs"))
                .addPrefixPath(contextPath + "/api-docs", webHandler)
                .addPrefixPath(contextPath, jaxrsServletHandler);

        server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(pathHandler)
                .build();


    }


    void start(){
        server.start();
        server.getListenerInfo().stream().forEach(listenerInfo -> logger.info("{} {}", listenerInfo.getProtcol(), listenerInfo.getAddress()));
        logger.info("Server started");
    }

    void stop(){
        server.stop();
        logger.info("Server stopped");
    }

    HttpHandler buildJAXRSServletHandler(final String contextPath) throws ServletException {

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(JAXRSServerV2.class.getClassLoader())
                .setContextPath(contextPath)
                .setDeploymentName("Swagger doc demo using annotation")
                .addServlets(Servlets.servlet("jersey", org.glassfish.jersey.servlet.ServletContainer.class)
                        .addInitParam("jersey.config.server.provider.packages", "io.swagger.jaxrs.listing,com.control2me.japi.services")
                        .setLoadOnStartup(1)
                        .addMapping("/services/*"))
                .addServlets(Servlets.servlet("SwaggerDocumentSetup", com.control2me.japi.SwaggerDocumenSetup.class)
                        .setLoadOnStartup(2));

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        //PathHandler path = Handlers.path(Handlers.redirect(contextPath))
        //       .addPrefixPath(contextPath, manager.start());
        HttpHandler path = manager.start();
        logger.info("Built JAXRSServletHandler to path {}", path);
        return path;
    }

    HttpHandler buildWebHandler() {
        final ClassPathResourceManager classPathResourceManager = new ClassPathResourceManager(getClass().getClassLoader(), "api-docs");
        final ResourceHandler resourceHandler = Handlers.resource(classPathResourceManager);
        logger.info("Built WebHandler to path {}", resourceHandler);
        return resourceHandler;
    }

}
