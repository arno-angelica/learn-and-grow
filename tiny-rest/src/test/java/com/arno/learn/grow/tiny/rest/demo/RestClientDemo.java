package com.arno.learn.grow.tiny.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClientDemo {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        Entity<String> entity = Entity.json("hello");
        Response response = client
                .target("http://127.0.0.1:8080/user/getAll")      // WebTarget
                .request() // Invocation.Builder
                .post(entity);                                     //  Response


        String content = response.readEntity(String.class);
        System.out.println(content);


        Response getResponse = client
                .target("http://127.0.0.1:8080/user/getAll")      // WebTarget
                .request() // Invocation.Builder
                .get();

        String getContent = getResponse.readEntity(String.class);
        System.out.println(getContent);

    }
}
