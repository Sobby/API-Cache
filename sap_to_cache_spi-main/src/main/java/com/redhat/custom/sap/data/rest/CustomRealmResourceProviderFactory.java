package com.redhat.custom.sap.data.rest;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class CustomRealmResourceProviderFactory implements RealmResourceProviderFactory {

    private static final Logger logger = Logger.getLogger(CustomRealmResourceProviderFactory.class);

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        logger.info("Creating factory >>>>>>");
        return new CustomRealmResourceProvider(session);
    }

    @Override
    public void init(Scope config) {
        logger.info("<<<<<< Initializing factory >>>>>>");
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "sap-data-storage-rest";
    }

}
