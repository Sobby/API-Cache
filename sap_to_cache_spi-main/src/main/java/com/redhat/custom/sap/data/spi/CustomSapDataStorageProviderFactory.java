package com.redhat.custom.sap.data.spi;

import java.util.Map;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderFactory;

public interface CustomSapDataStorageProviderFactory extends ProviderFactory<CustomSapDataStorageService> {

    void init(Config.Scope config);

    CustomSapDataStorageService create(KeycloakSession session);

    void postInit(KeycloakSessionFactory factory);

    void close();

    String getId();

    Map<String, String> getOperationalInfo();
}
