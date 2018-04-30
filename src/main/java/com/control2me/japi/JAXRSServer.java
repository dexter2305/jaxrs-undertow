package com.control2me.japi;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;

public class JAXRSServer {

    private static final Logger logger = LoggerFactory.getLogger(JAXRSServer.class);
    private final UndertowJaxrsServer server = new UndertowJaxrsServer();

    public JAXRSServer(Integer port, String host) {
        Undertow.Builder serverBuilder = Undertow.builder().addHttpListener(port, host);
        server.start(serverBuilder);
    }

    /**
     * Use deployApplication method that requires only the DeploymentInfo instance.
     * @param appPath
     * @param applicationClass
     * @return
     */
    @Deprecated
    public DeploymentInfo deployApplication(String appPath, Class<? extends Application> applicationClass) {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplicationClass(applicationClass.getName());
        return server.undertowDeployment(deployment, appPath);
    }

    public DeploymentInfo deployApplication(Class<? extends Application> applicationClass){
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplicationClass(applicationClass.getName());
        final DeploymentInfo deploymentInfo = server.undertowDeployment(deployment);
        logger.info("Deployment info {}", deploymentInfo.toString());
        return deploymentInfo;
    }

    public void deploy(DeploymentInfo deploymentInfo) {
        server.deploy(deploymentInfo);
    }

    public static void main(String[] args) {
        JAXRSServer myServer = new JAXRSServer(8080, "0.0.0.0");
        DeploymentInfo di = myServer.deployApplication(JAXRSApplication.class)
                .setClassLoader(JAXRSServer.class.getClassLoader())
                .setContextPath("/demo")
                .setDeploymentName("Swagger documentation through annotation")
                .addServlets(Servlets.servlet("jersey", org.glassfish.jersey.servlet.ServletContainer.class)
                        .addInitParam("jersey.config.server.provider.packages", "io.swagger.jaxrs.listing,com.control2me.japi.services")
                        .setLoadOnStartup(1)
                        .addMapping("/services/*"))
                .addServlets(Servlets.servlet("SwaggerDocumentSetup", com.control2me.japi.SwaggerDocumenSetup.class)
                        .setLoadOnStartup(2));
        //.addServlets(Servlets.servlet("helloServlet", org.viddu.poc.HelloServlet.class).setLoadOnStartup(1).addMapping("/"))
        //.addServlets(Servlets.servlet("swagger-servlet", org.dl.poc.SwaggerDocumentSetup.class).setLoadOnStartup(2));
        myServer.deploy(di);
    }
}
