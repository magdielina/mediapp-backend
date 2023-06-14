package com.mitocode.security;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeyCloakConfig {
    public static Keycloak keycloak = null;
//    public final static String serverUrl = "http://localhost:9898/auth"; //KeyCloak Wildfly
    public final static String serverUrl = "http://localhost:9898"; //KeyCloak QUARKUS
    public final static String realm = "mediapp";
    public final static String clientId = "mediapp-backend";
    public final static String clientSecret = ""; //necesario en confidencial
    //public final static String userRootName = "magdiellinares@gmail.com"; //Usuario root o con privilegios necesarios si quiero uno fijo
    public final static String password = "123";

    public KeyCloakConfig() {
    }

    public static Keycloak getInstance(String userName){
        if(keycloak == null){

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    //.authorization("authorization")
                    //.clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilderImpl() //new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build()
                    )
                    .build();
        }
        return keycloak;
    }
}
