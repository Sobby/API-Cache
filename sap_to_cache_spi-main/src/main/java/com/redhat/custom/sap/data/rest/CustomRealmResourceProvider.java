package com.redhat.custom.sap.data.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class CustomRealmResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public CustomRealmResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void close() {

    }

    @Override
    public Object getResource() {
        // CustomSapDataStorageService provider = new CustomSapDataStorageProviderFactoryImpl().create(session);
        return new CustomRestResource(session);
    }

}
