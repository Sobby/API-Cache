package com.redhat.custom.sap.data.spi;

import org.keycloak.provider.Provider;

import com.redhat.custom.sap.data.SapDataRepresentation;

import org.keycloak.broker.provider.util.SimpleHttp.Response;

public interface CustomSapDataStorageService extends Provider {

    void setApiEndpoint(String apiEndpoint);

    void setApiKey(String apiKey);

    SapDataRepresentation parseData(Response response, String userId);

    Response getRequest();

}
