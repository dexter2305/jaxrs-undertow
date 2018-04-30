package com.control2me.japi.services.v1;

import javax.xml.bind.annotation.XmlRootElement;


public class User {
    private static final long serialVersionUID = 1L;
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
