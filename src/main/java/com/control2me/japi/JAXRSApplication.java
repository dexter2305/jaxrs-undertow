package com.control2me.japi;

import org.viddu.poc.HelloResource;

import javax.ws.rs.core.Application;
import java.util.LinkedHashSet;
import java.util.Set;

public class JAXRSApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new LinkedHashSet<Class<?>>();
        resources.add(HelloResource.class);
        return resources;
    }



}
